package com.lldpractice.messagequeue.message;

import java.util.List;

import com.lldpractice.messagequeue.consumer.ConsumerProperties;
import com.lldpractice.messagequeue.producer.ProducerProperties;

public interface MessageHandler {
    void handlePublishRequest(ProducerProperties producerProperties, Message message);

    List<Message> handlePollRequest(ConsumerProperties consumerProperties);

    void handleConsumerAcknowledgeRequest(ConsumerProperties consumerProperties, int consumedMessages);
}
