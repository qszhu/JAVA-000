package io.github.qszhu.database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItem {
    private String id;
    private Item item;
    private int quantity;
}
