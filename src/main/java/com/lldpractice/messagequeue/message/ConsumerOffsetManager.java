package com.lldpractice.messagequeue.message;

import com.lldpractice.messagequeue.consumer.ConsumerProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsumerOffsetManager {
    private ConsumerProperties consumerProperties;
    private int offset;
}
