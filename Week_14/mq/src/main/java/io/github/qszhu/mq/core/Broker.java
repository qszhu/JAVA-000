package io.github.qszhu.mq.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Broker {
    private int capacity;
    private final Map<String, MessageQueue> queueMap = new ConcurrentHashMap<>();

    public Broker() {
        this(10000);
    }

    public Broker(int capacity) {
        this.capacity = capacity;
    }

    public void createTopic(String topic) {
        queueMap.putIfAbsent(topic, new MessageQueue(topic, capacity));
    }

    public MessageQueue getQueue(String topic) {
        return queueMap.get(topic);
    }

    public Producer createProducer() {
        return new Producer(this);
    }

    public Consumer createConsumer() {
        return new Consumer(this);
    }
}
