package com.lldpractice.messagequeue.producer;

import com.lldpractice.messagequeue.message.Message;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Producer {
    private String id, name;
    private ProducerProperties properties;

    public void publishMessage(Message message) {
        this.properties.getBroker().producerPublish(properties, message);
    }
}
