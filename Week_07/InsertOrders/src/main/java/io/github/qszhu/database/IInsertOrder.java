package io.github.qszhu.database;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IInsertOrder {
    void insert(List<Order> orders) throws SQLException, ExecutionException, InterruptedException;
}
