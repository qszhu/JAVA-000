package io.github.qszhu.mq.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class Message<T> {
    private Map<String, Object> headers;
    private T body;

    public Message(T body) {
        this(null, body);
    }
}
