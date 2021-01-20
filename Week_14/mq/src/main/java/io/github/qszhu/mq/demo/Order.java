package io.github.qszhu.mq.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private long id;
    private long ts;
    private String symbol;
    private long price;
}
