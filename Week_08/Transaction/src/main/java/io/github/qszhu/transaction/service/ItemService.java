package io.github.qszhu.transaction.service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemService {
    @Resource(name = "itemConnection")
    private Connection conn;

    public void reduce(int id, int quantity) throws SQLException {
        String sql = "update item set stock=stock-? where id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, quantity);
            pst.setInt(2, id);
            int rowAffected = pst.executeUpdate();
            if (rowAffected != 1) throw new SQLException("Something wrong");
        }
    }

    public int getRemain(int id) throws SQLException {
        String sql = "select stock from item where id=?";
        ResultSet rs = null;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.execute();
            rs = pst.getResultSet();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Result not found");
        } finally {
            if (rs != null) rs.close();
        }
    }
}
