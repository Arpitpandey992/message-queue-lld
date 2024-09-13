package com.lldpractice.messagequeue.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {
    private String producerId, message;
}
