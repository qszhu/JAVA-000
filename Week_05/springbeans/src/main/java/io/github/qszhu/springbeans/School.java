package io.github.qszhu.springbeans;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Data
public class School implements ISchool {
    @Autowired(required = true)
    Klass class1;

    @Resource(name = "student100")
    Student student100;

    public void ding() {
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);
    }
}
