package com.lldpractice.messagequeue.consumer;

import java.util.List;
import java.util.UUID;

import com.lldpractice.messagequeue.message.Message;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
@Getter
public class Consumer {
    @Default
    private String id = UUID.randomUUID().toString();
    ConsumerProperties properties;

    public List<Message> pollMessages() {
        return this.properties.getBroker().consumerPoll(this.properties);
    }

    public void acknowledgeMessages(int messageCount) {
        this.properties.getBroker().consumerAcknowledge(properties, messageCount);
    }

    public String getName() {
        return properties.getName();
    }
}
