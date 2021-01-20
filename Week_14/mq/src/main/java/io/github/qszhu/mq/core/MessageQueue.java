package io.github.qszhu.mq.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueue {
    private String topic;
    private int capacity;
    private LinkedBlockingQueue<Message> queue;

    MessageQueue(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue<>();
    }

    public boolean send(Message msg) {
        return queue.offer(msg);
    }

    public Message poll() {
        return queue.poll();
    }

    public Message poll(long timeout) throws InterruptedException {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }
}
