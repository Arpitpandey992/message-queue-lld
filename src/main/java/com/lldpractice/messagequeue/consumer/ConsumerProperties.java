package com.lldpractice.messagequeue.consumer;

import java.util.UUID;

import com.lldpractice.messagequeue.Broker;

import lombok.Builder;
import lombok.Getter;
import lombok.Builder.Default;

@Getter
@Builder
public class ConsumerProperties {
    @Default
    private String id = UUID.randomUUID().toString();
    private String name, topicName;
    private Broker broker;
}