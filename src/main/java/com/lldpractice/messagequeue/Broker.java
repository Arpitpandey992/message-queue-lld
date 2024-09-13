package com.lldpractice.messagequeue;

import java.util.List;

import com.lldpractice.messagequeue.consumer.ConsumerProperties;
import com.lldpractice.messagequeue.message.InMemoryMessageHandler;
import com.lldpractice.messagequeue.message.Message;
import com.lldpractice.messagequeue.message.MessageHandler;
import com.lldpractice.messagequeue.producer.ProducerProperties;
import com.lldpractice.messagequeue.topic.TopicManager;

public class Broker {
    TopicManager topicManager = new TopicManager();
    MessageHandler messageHandler = new InMemoryMessageHandler(topicManager);

    public List<Message> consumerPoll(ConsumerProperties consumerProperties) {
        return this.messageHandler.handlePollRequest(consumerProperties);
    }

    public void consumerAcknowledge(ConsumerProperties consumerProperties, int consumedMessages) {
        this.messageHandler.handleConsumerAcknowledgeRequest(consumerProperties, consumedMessages);
    }

    public void producerPublish(ProducerProperties producerProperties, Message message) {
        this.messageHandler.handlePublishRequest(producerProperties, message);
    }
}
