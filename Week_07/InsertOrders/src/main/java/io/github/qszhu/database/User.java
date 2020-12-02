package io.github.qszhu.database;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String phone;
    private String address;

    public static User random() {
        return new User(
                Util.newId(),
                Util.randomString(5),
                Util.randomString(11),
                Util.randomString(30)
        );
    }

    public static List<User> randomList(int num) {
        List<User> res = new ArrayList<>(num);
        for (int i = 0; i < num; i++) res.add(random());
        return res;
    }
}
