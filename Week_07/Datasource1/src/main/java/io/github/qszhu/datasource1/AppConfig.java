package io.github.qszhu.datasource1;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    public DataSource getReadWriteDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:4406/shop");
        config.setUsername("root");
        config.setPassword("example");
        config.setMaximumPoolSize(50);
        return new HikariDataSource(config);
    }

    public DataSource getReadOnlyDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:4407/shop");
        config.setUsername("root");
        config.setPassword("example");
        config.setMaximumPoolSize(50);
        return new HikariDataSource(config);
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBType.READ_WRITE, getReadWriteDataSource());
        targetDataSources.put(DBType.READONLY, getReadOnlyDataSource());

        DataSourceRouter router = new DataSourceRouter();
        router.setTargetDataSources(targetDataSources);
        return router;
    }
}
