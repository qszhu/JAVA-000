package io.github.qszhu.transaction.service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    @Resource(name = "userConnection")
    private Connection conn;

    public void freeze(int id, int amount) throws SQLException {
        System.out.println("Freeze user " + id + " balance with amount " + amount);
    }

    public void unfreeze(int id, int amount) throws SQLException {
        System.out.println("Unfreeze user " + id + " balance with amount " + amount);
    }

    public void withdraw(int id, int amount) throws SQLException {
        String sql = "update user set balance=balance-? where id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, amount);
            pst.setInt(2, id);
            int rowAffected = pst.executeUpdate();
            if (rowAffected != 1) throw new SQLException("Something wrong");
        }
        System.out.println("Withdraw user " + id + " balance with amount " + amount);
    }

    public int getBalance(int id) throws SQLException {
        String sql = "select balance from user where id=?";
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
