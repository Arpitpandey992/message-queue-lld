package com.lldpractice.messagequeue.producer;

import java.util.UUID;

import com.lldpractice.messagequeue.message.Message;

import lombok.Builder;
import lombok.Getter;
import lombok.Builder.Default;

@Builder
@Getter
public class Producer {
    @Default
    private String id = UUID.randomUUID().toString();
    private ProducerProperties properties;

    public void publishMessage(Message message) {
        this.properties.getBroker().producerPublish(properties, message);
    }

    public String getName() {
        return properties.getName();
    }
}
