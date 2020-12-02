package io.github.qszhu.database;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String description;
    private int price;

    public static Item random() {
        return new Item(
                Util.newId(),
                Util.randomString(10),
                Util.randomString(50),
                Util.randomInt(100000)
        );
    }

    public static List<Item> randomList(int num) {
        List<Item> res = new ArrayList<>(num);
        for (int i = 0; i < num; i++) res.add(random());
        return res;
    }
}
