package io.github.qszhu.transaction;

import io.github.qszhu.transaction.service.ItemService;
import io.github.qszhu.transaction.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class AppConfig {
    @Bean(name = "userConnection")
    public Connection getUserConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_user", "root", "toor");
    }

    @Bean(name = "itemConnection")
    public Connection getItemConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_item", "root", "toor");
    }

    @Bean(name = "userService")
    public UserService getUserService() {
        return new UserService();
    }

    @Bean(name = "itemService")
    public ItemService getItemService() {
        return new ItemService();
    }
}
