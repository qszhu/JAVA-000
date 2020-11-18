package io.github.qszhu.springbootdata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
