package com.lldpractice.messagequeue.producer;

import com.lldpractice.messagequeue.Broker;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducerProperties {
    private String id, name, topicName;
    private Broker broker;
}
