package io.github.qszhu.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class AutoConfiguration {
    @Bean("student123")
    public Student getStudent123() {
        return new Student(123, "KK123");
    }

    @Bean("student100")
    public Student getStudent100() {
        return new Student(100, "KK100");
    }

    @Bean("class1")
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
