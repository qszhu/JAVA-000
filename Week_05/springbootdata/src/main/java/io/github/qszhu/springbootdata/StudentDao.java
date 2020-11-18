package io.github.qszhu.springbootdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource(name = "student100")
    private Student student100;

    @Resource(name = "student123")
    private Student student123;

    public void batchInsert() {
        List<Student> list = Arrays.asList(student100, student123);
        namedParameterJdbcTemplate.batchUpdate("INSERT INTO STUDENT (ID, NAME) VALUES (:id, :name)",
                SqlParameterSourceUtils.createBatch(list));
    }

    @Transactional
    public void dupInsert() {
        List<Student> list = Arrays.asList(new Student(42, "qinsi"));
        namedParameterJdbcTemplate.batchUpdate("INSERT INTO STUDENT (ID, NAME) VALUES (:id, :name)",
                SqlParameterSourceUtils.createBatch(list));
        batchInsert();
    }

    public void listData() {
        List<Student> fooList = jdbcTemplate.query("SELECT * FROM STUDENT", new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Student(rs.getInt(1), rs.getString(2));
            }
        });
        fooList.forEach(f -> System.out.printf("Foo: %s\n", f));
    }
}
