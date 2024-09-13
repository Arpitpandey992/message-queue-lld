package com.lldpractice.messagequeue.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.lldpractice.messagequeue.consumer.ConsumerProperties;
import com.lldpractice.messagequeue.producer.ProducerProperties;
import com.lldpractice.messagequeue.topic.TopicManager;

public class InMemoryMessageHandler implements MessageHandler {
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = reentrantReadWriteLock.readLock();
    private Lock writeLock = reentrantReadWriteLock.writeLock();
    private Map<String, ConsumerOffsetManager> consumerOffsetMap;

    private TopicManager topicManager;

    public InMemoryMessageHandler(TopicManager topicManager) {
        this.topicManager = topicManager;
        this.consumerOffsetMap = new HashMap<>();
    }

    @Override
    public void handlePublishRequest(ProducerProperties producerProperties, Message message) {
        this.topicManager.appendToTopic(producerProperties.getTopicName(), message);
    }

    @Override
    public List<Message> handlePollRequest(ConsumerProperties consumerProperties) {
        readLock.lock();
        try {
            ConsumerOffsetManager offsetManager = consumerOffsetMap.getOrDefault(consumerProperties.getId(),
                    new ConsumerOffsetManager(consumerProperties, 0));
            return topicManager.getAllMessagesFrom(offsetManager.getConsumerProperties().getTopicName(),
                    offsetManager.getOffset());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void handleConsumerAcknowledgeRequest(ConsumerProperties consumerProperties, int consumedMessages) {
        writeLock.lock();
        try {
            if (!this.consumerOffsetMap.containsKey(consumerProperties.getId())) {
                System.out.printf("Invalid acknowledge request from consumer: %s", consumerProperties.getName());
                return;
            }
            ConsumerOffsetManager offsetManager = this.consumerOffsetMap.get(consumerProperties.getId());
            offsetManager.setOffset(offsetManager.getOffset() + consumedMessages);
        } finally {
            writeLock.unlock();
        }
    }

}
