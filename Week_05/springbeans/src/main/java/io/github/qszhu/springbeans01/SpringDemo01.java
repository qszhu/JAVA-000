package io.github.qszhu.springbeans01;

import io.github.qszhu.springbeans.ISchool;
import io.github.qszhu.springbeans.Klass;
import io.github.qszhu.springbeans.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringDemo01 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Student student123 = (Student) context.getBean("student123");
        System.out.println(student123.toString());

        Student student100 = (Student) context.getBean("student100");
        System.out.println(student100.toString());

        Klass class1 = context.getBean(Klass.class);
        System.out.println(class1);

        ISchool school = context.getBean(ISchool.class);
        System.out.println(school);

        school.ding();

        class1.dong();

        System.out.println("   context.getBeanDefinitionNames() ===>> " + String.join(",", context.getBeanDefinitionNames()));
    }
}
