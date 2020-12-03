package io.github.qszhu.database;

import lombok.AllArgsConstructor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class InsertOrderPooling implements IInsertOrder {
    @Resource(name = "dataSource")
    private DataSource ds;

    private static final int poolSize = 20;
    private static final int batchSize = 4000;

    @AllArgsConstructor
    private static class BatchRunner implements Runnable {
        private final Connection conn;
        private final PreparedStatement pst;

        @Override
        public void run() {
            try {
                try {
                    this.pst.executeBatch();
                } finally {
                    this.conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void insert(List<Order> orders) throws SQLException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        List<Future<?>> res = new ArrayList<>();

        String orderSql = "INSERT INTO `order` (id, userId) values (?, ?)";
        String orderItemSql = "INSERT INTO order_item (id, itemId, quantity, orderId) values (?, ?, ?, ?)";


        Connection conn = ds.getConnection();
        PreparedStatement orderPst = conn.prepareStatement(orderSql);

        int i = 0;
        for (Order order : orders) {
            orderPst.setString(1, order.getId());
            orderPst.setString(2, order.getUser().getId());
            orderPst.addBatch();
            i++;
            if (i % batchSize == 0) {
                Connection conn1 = conn;
                PreparedStatement pst = orderPst;

                conn = ds.getConnection();
                orderPst = conn.prepareStatement(orderSql);

                Future<?> f = executor.submit(new BatchRunner(conn1, pst));
                res.add(f);
            }
        }
        {
            Future<?> f = executor.submit(new BatchRunner(conn, orderPst));
            res.add(f);
        }

        for (Future<?> f : res) f.get();

        i = 0;
        res.clear();

        conn = ds.getConnection();
        PreparedStatement orderItemPst = conn.prepareStatement(orderItemSql);

        for (Order order : orders) {
            for (OrderItem orderItem : order.getItems()) {
                orderItemPst.setString(1, orderItem.getId());
                orderItemPst.setString(2, orderItem.getItem().getId());
                orderItemPst.setInt(3, orderItem.getQuantity());
                orderItemPst.setString(4, order.getId());
                orderItemPst.addBatch();
                i++;
                if (i % batchSize == 0) {
                    Connection conn1 = conn;
                    PreparedStatement pst = orderItemPst;

                    conn = ds.getConnection();
                    orderItemPst = conn.prepareStatement(orderItemSql);

                    Future<?> f = executor.submit(new BatchRunner(conn1, pst));
                    res.add(f);
                }
            }
        }
        {
            Future<?> f = executor.submit(new BatchRunner(conn, orderItemPst));
            res.add(f);
        }

        for (Future<?> f : res) f.get();

        executor.shutdown();
    }
}
