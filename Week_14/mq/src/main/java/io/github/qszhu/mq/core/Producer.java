package io.github.qszhu.mq.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Producer {
    private Broker broker;

    public boolean send(String topic, Message msg) throws MessageQueueException {
        MessageQueue mq = broker.getQueue(topic);
        if (mq == null) throw new MessageQueueException(String.format("Topic [%s] does not exist.", topic));
        return mq.send(msg);
    }

}
