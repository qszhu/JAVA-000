package io.github.qszhu.springbootdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
@EnableTransactionManagement
public class DataDemo implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private StudentDao studentDao;

    public static void main(String[] args) {
        SpringApplication.run(DataDemo.class, args);
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(dataSource.toString());
        Connection conn = dataSource.getConnection();
        System.out.println(conn.toString());
        conn.close();

        studentDao.batchInsert();
        try {
            studentDao.dupInsert();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        studentDao.listData();
    }

}
