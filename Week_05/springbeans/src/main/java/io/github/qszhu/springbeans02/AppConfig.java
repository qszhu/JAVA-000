package io.github.qszhu.springbeans02;

import io.github.qszhu.springbeans.ISchool;
import io.github.qszhu.springbeans.Klass;
import io.github.qszhu.springbeans.School;
import io.github.qszhu.springbeans.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean(name = "student123")
    public Student getStudent123() {
        return new Student(123, "KK123");
    }

    @Bean(name = "student100")
    public Student getStudent100() {
        return new Student(100, "KK100");
    }

    @Bean(name = "class1")
    public Klass getKlass() {
        Klass res = new Klass();
        res.setStudents(Arrays.asList(getStudent100(), getStudent123()));
        return res;
    }

    @Bean
    public ISchool getSchool() {
        return new School();
    }
}
