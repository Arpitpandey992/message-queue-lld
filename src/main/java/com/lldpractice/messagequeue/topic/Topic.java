package com.lldpractice.messagequeue.topic;

import java.util.List;

import com.lldpractice.messagequeue.message.Message;

import lombok.Getter;

public abstract class Topic {
    @Getter
    String id, name;

    abstract public int getCurrentOffset();

    abstract public void appendMessage(Message message);

    abstract public List<Message> getMessages(int fromOffset, int toOffset);
}
