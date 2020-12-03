package io.github.qszhu.database;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.*;
import java.util.List;

public class InsertOrders {
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
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO user (id, name, phone, address) values (?, ?, ?, ?)");
        for (User user : users) {
            pst.setString(1, user.getId());
            pst.setString(2, user.getName());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getAddress());
            pst.execute();
        }
        conn.commit();
    }

    private static void insertItems(Connection conn, List<Item> items) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO item (id, name, description, price) values (?, ?, ?, ?)");
        for (Item item : items) {
            pst.setString(1, item.getId());
            pst.setString(2, item.getName());
            pst.setString(3, item.getDescription());
            pst.setInt(4, item.getPrice());
            pst.execute();
        }
        conn.commit();
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        List<User> users = User.randomList(100);
        List<Item> items = Item.randomList(1000);

        int numOrders = 1_000_000;
        List<Order> orders = Order.randomList(numOrders, users, items, 10);

        System.out.println("preparing");
        try (
                Connection conn = (Connection) context.getBean("mysqlConnection")
        ) {
            cleanDb(conn);
            insertUsers(conn, users);
            insertItems(conn, items);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("started");
        try (
                Connection conn = (Connection) context.getBean("mysqlConnection")
        ) {
//            IInsertOrder io = (IInsertOrder) context.getBean("InsertOrderSingleStatements");
//            IInsertOrder io = (IInsertOrder) context.getBean("InsertOrderBatchStatements");
//            IInsertOrder io = (IInsertOrder) context.getBean("InsertOrderBatchNoAutoCommit");
//            IInsertOrder io = (IInsertOrder) context.getBean("InsertOrderRewriteBatchedStatements");
            IInsertOrder io = (IInsertOrder) context.getBean("InsertOrderPooling");
            long t1 = System.currentTimeMillis();
            io.insert(orders);
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
