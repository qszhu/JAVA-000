package io.github.qszhu.datasource1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ReadWriteManual {
    private static void insertUser(Connection conn) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO user (id, name, phone, address) values (?, ?, ?, ?)");
        pst.setString(1, UUID.randomUUID().toString());
        pst.setString(2, "foo");
        pst.setString(3, "123");
        pst.setString(4, "bar");
        pst.execute();
    }

    private static void readUser(Connection conn) throws SQLException {
        ResultSet rs = conn.createStatement()
                .executeQuery("select count(id) as total from user");
        rs.next();
        System.out.println(rs.getInt("total"));
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        DataSource ds = (DataSource) context.getBean("dataSource");

        DBContextHolder.set(DBType.READ_WRITE);
        try (Connection conn = ds.getConnection()) {
            System.out.println("Writing to " + conn.getMetaData().getURL());
            insertUser(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBContextHolder.set(DBType.READONLY);
        try (Connection conn = ds.getConnection()) {
            System.out.println("Reading from " + conn.getMetaData().getURL());
            readUser(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
