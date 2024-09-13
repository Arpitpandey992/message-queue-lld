package com.lldpractice.messagequeue.consumer;

import java.util.List;

import com.lldpractice.messagequeue.message.Message;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Consumer {
    private String id, name;
    ConsumerProperties properties;

    List<Message> pollMessages() {
        return this.properties.getBroker().consumerPoll(this.properties);
    }
}
