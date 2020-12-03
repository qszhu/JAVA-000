package io.github.qszhu.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class AppConfig {
    @Bean(name = "mysqlConnection")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Connection getMysqlConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "toor");
    }

    @Bean(name = "mysqlBatchConnection")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Connection getMysqlBatchConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/shop?rewriteBatchedStatements=true",
                "root", "toor");
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/shop?rewriteBatchedStatements=true");
        config.setUsername("root");
        config.setPassword("toor");
        config.setMaximumPoolSize(50);
        return new HikariDataSource(config);
    }

    @Bean(name = "InsertOrderSingleStatements")
    public IInsertOrder getInsertOrderSingleStatements() {
        return new InsertOrderSingleStatements();
    }

    @Bean(name = "InsertOrderBatchStatements")
    public IInsertOrder getInsertOrderBatchStatements() {
        return new InsertOrderBatchStatements();
    }

    @Bean(name = "InsertOrderBatchNoAutoCommit")
    public IInsertOrder getInsertOrderBatchNoAutoCommit() {
        return new InsertOrderBatchNoAutoCommit();
    }

    @Bean(name = "InsertOrderRewriteBatchedStatements")
    public IInsertOrder getInsertOrderRewriteBatchedStatements() {
        return new InsertOrderRewriteBatchedStatements();
    }

    @Bean(name = "InsertOrderPooling")
    public IInsertOrder getInsertOrderPooling() {
        return new InsertOrderPooling();
    }
}
