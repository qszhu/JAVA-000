package io.github.qszhu.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class SpringBootDemo implements ApplicationRunner {
    @Resource(name = "student100")
    private Student student100;

    @Resource(name = "student123")
    private Student student123;

    @Autowired
    private Klass class1;

    @Autowired
    private ISchool school;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(student123.toString());

        System.out.println(student100.toString());

        System.out.println(class1);

        System.out.println(school);

        school.ding();

        class1.dong();
    }
}
