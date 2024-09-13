package com.lldpractice.messagequeue.consumer;

import com.lldpractice.messagequeue.Broker;

import lombok.Data;

@Data
public class ConsumerProperties {
    private String id, name, topicName;
    private Broker broker;
}
