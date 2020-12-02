package io.github.qszhu.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertOrders {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/shop";
    private static final String mysqlUser = "root";
    private static final String mysqlPass = "toor";

    private static void cleanDb(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        Statement st = conn.createStatement();

        st.execute("DELETE FROM order_item");
        st.execute("DELETE FROM `order`");
        st.execute("DELETE FROM user");
        st.execute("DELETE FROM item");
        conn.commit();
    }

    private static void insertUsers(Connection conn, List<User> users) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO user (id, name, phone, address) values (?, ?, ?, ?)");
        for (User user : users) {
            pst.setString(1, user.getId());
            pst.setString(2, user.getName());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getAddress());
            pst.execute();
        }
    }

    private static void insertItems(Connection conn, List<Item> items) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO item (id, name, description, price) values (?, ?, ?, ?)");
        for (Item item : items) {
            pst.setString(1, item.getId());
            pst.setString(2, item.getName());
            pst.setString(3, item.getDescription());
            pst.setInt(4, item.getPrice());
            pst.execute();
        }
    }

    private static void insertOrders1(Connection conn, List<Order> orders) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO `order` (id, userId) values (?, ?)");
        PreparedStatement pst1 = conn.prepareStatement(
                "INSERT INTO order_item (id, itemId, quantity, orderId) values (?, ?, ?, ?)");
        for (Order order : orders) {
            pst.setString(1, order.getId());
            pst.setString(2, order.getUser().getId());
            pst.execute();

            for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                pst1.setString(1, Util.newId());
                pst1.setString(2, item.getId());
                pst1.setInt(3, quantity);
                pst1.setString(4, order.getId());
                pst1.execute();
            }
        }
    }

    private static void insertOrders2(Connection conn, List<Order> orders) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO `order` (id, userId) values (?, ?)");
        PreparedStatement pst1 = conn.prepareStatement(
                "INSERT INTO order_item (id, itemId, quantity, orderId) values (?, ?, ?, ?)");
        for (Order order : orders) {
            pst.setString(1, order.getId());
            pst.setString(2, order.getUser().getId());
            pst.addBatch();
        }
        pst.executeBatch();

        for (Order order : orders) {
            for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                pst1.setString(1, Util.newId());
                pst1.setString(2, item.getId());
                pst1.setInt(3, quantity);
                pst1.setString(4, order.getId());
                pst1.addBatch();
            }
        }
        pst1.executeBatch();
    }

    private static void insertOrders3(Connection conn, List<Order> orders) throws SQLException {
        conn.setAutoCommit(false);

        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO `order` (id, userId) values (?, ?)");
        PreparedStatement pst1 = conn.prepareStatement(
                "INSERT INTO order_item (id, itemId, quantity, orderId) values (?, ?, ?, ?)");
        for (Order order : orders) {
            pst.setString(1, order.getId());
            pst.setString(2, order.getUser().getId());
            pst.addBatch();
        }
        pst.executeBatch();

        for (Order order : orders) {
            for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                pst1.setString(1, Util.newId());
                pst1.setString(2, item.getId());
                pst1.setInt(3, quantity);
                pst1.setString(4, order.getId());
                pst1.addBatch();
            }
        }
        pst1.executeBatch();

        conn.commit();
    }

    private static void insertOrders4(Connection conn, List<Order> orders) throws SQLException {
        conn.setAutoCommit(false);

        Statement st = conn.createStatement();
        PreparedStatement pst1 = conn.prepareStatement(
                "INSERT INTO order_item (id, itemId, quantity, orderId) values (?, ?, ?, ?)");

        int batchSize = 1000;

        int i = 0;
        List<String> values = new ArrayList<>();
        for (Order order : orders) {
            values.add(String.format("(\"%s\", \"%s\")", order.getId(), order.getUser().getId()));

            i++;
            if (i % batchSize == 0) {
                st.execute(String.format("INSERT INTO `order` (id, userId) values %s",
                        String.join(",", values)));
                values.clear();
            }
        }
        if (!values.isEmpty()) {
            st.execute(String.format("INSERT INTO `order` (id, userId) values %s",
                    String.join(",", values)));
        }

        i = 0;
        values.clear();
        for (Order order : orders) {
            for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                values.add(String.format("(\"%s\", \"%s\", %d, \"%s\")", Util.newId(), item.getId(), quantity, order.getId()));

                i++;
                if (i % batchSize == 0) {
                    st.execute(String.format("INSERT INTO order_item (id, itemId, quantity, orderId) values %s",
                            String.join(",", values)));
                    values.clear();
                }
            }
        }
        if (!values.isEmpty()) {
            st.execute(String.format("INSERT INTO order_item (id, itemId, quantity, orderId) values %s",
                    String.join(",", values)));
        }

        conn.commit();
    }

    public static void main(String[] args) throws Exception {
        List<User> users = User.randomList(100);
        List<Item> items = Item.randomList(1000);

        int numOrders = 1_000_000;
        List<Order> orders = Order.randomList(numOrders, users, items, 10);

        try {
            Connection conn = DriverManager.getConnection(connectionUrl, mysqlUser, mysqlPass);

            System.out.println("preparing");
            cleanDb(conn);

            insertUsers(conn, users);
            insertItems(conn, items);

            System.out.println("started");
            long t1 = System.currentTimeMillis();
//            insertOrders1(conn, orders);
//            insertOrders2(conn, orders);
//            insertOrders3(conn, orders);
            insertOrders4(conn, orders);
            long t2 = System.currentTimeMillis();

            ResultSet rs = conn.createStatement().executeQuery("select count(id) as total from order_item");
            rs.next();
            System.out.println(rs.getInt("total"));

            System.out.println("" + (t2 - t1) + " ms");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
