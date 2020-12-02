package io.github.qszhu.database;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private User user;
    private Map<Item, Integer> items;

    public static Order random(List<User> users, List<Item> items, int maxItems) {
        Random r = new Random();

        User user = users.get(r.nextInt(users.size()));

        int numItems = r.nextInt(maxItems) + 1;
        Map<Item, Integer> selItems = new HashMap<>(numItems);
        for (int i = 0; i < numItems; i++) {
            Item item = items.get(r.nextInt(items.size()));
            int quantity = selItems.getOrDefault(item, 0);
            selItems.put(item, quantity + 1);
        }

        return new Order(
                Util.newId(),
                user,
                selItems
        );
    }

    public static List<Order> randomList(int num, List<User> users, List<Item> items, int maxItems) {
        List<Order> res = new ArrayList<>(num);
        for (int i = 0; i < num; i++) res.add(random(users, items, maxItems));
        return res;
    }
}

