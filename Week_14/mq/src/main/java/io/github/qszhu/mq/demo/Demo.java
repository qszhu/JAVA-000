package io.github.qszhu.mq.demo;

import io.github.qszhu.mq.core.Broker;
import io.github.qszhu.mq.core.Consumer;
import io.github.qszhu.mq.core.Message;
import io.github.qszhu.mq.core.Producer;

public class Demo {
    private static boolean running;

    public static void main(String[] args) throws Exception {
        String topic = "test";
        Broker broker = new Broker();
        broker.createTopic(topic);

        Consumer consumer = broker.createConsumer();
        consumer.subscribe(topic);

        running = true;
        new Thread(() -> {
            while (running) {
                Message<Order> msg = consumer.poll(100);
                if (msg != null) {
                    System.out.println(msg.getBody());
                }
            }
        }).start();

        Producer producer = broker.createProducer();
        for (int i = 0; i < 1000; i++) {
            Order order = new Order(1000 + i, System.currentTimeMillis(), "USD2CNY", 65100);
            producer.send(topic, new Message<>(order));
        }
        Thread.sleep(500);
        running = false;
    }
}
