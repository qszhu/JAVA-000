package io.github.qszhu.database;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertOrderBatchNoAutoCommit implements IInsertOrder {
    @Resource(name = "mysqlConnection")
    private Connection conn;

    @Override
    public void insert(List<Order> orders) throws SQLException {
        conn.setAutoCommit(false);

        PreparedStatement orderPst = conn.prepareStatement(
                "INSERT INTO `order` (id, userId) values (?, ?)");
        PreparedStatement orderItemPst = conn.prepareStatement(
                "INSERT INTO order_item (id, itemId, quantity, orderId) values (?, ?, ?, ?)");
        for (Order order : orders) {
            orderPst.setString(1, order.getId());
            orderPst.setString(2, order.getUser().getId());
            orderPst.addBatch();
        }
        orderPst.executeBatch();

        for (Order order : orders) {
            for (OrderItem orderItem : order.getItems()) {
                orderItemPst.setString(1, orderItem.getId());
                orderItemPst.setString(2, orderItem.getItem().getId());
                orderItemPst.setInt(3, orderItem.getQuantity());
                orderItemPst.setString(4, order.getId());
                orderItemPst.addBatch();
            }
        }
        orderItemPst.executeBatch();

        conn.commit();
        conn.close();
    }
}
