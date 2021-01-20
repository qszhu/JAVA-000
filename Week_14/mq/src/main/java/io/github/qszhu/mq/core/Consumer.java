package io.github.qszhu.mq.core;

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

    public Message poll(long timeout) throws InterruptedException {
        return mq.poll(timeout);
    }
}
