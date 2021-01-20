package io.github.qszhu.mq.core;

import lombok.SneakyThrows;

public class Consumer {
    private Broker broker;
    private MessageQueue mq;

    Consumer(Broker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) throws MessageQueueException {
        mq = broker.getQueue(topic);
        if (mq == null) throw new MessageQueueException(String.format("Topic [%s] does not exist.", topic));
    }

    @SneakyThrows
    public <T> Message<T> poll(long timeout) {
        return mq.poll(timeout);
    }
}
