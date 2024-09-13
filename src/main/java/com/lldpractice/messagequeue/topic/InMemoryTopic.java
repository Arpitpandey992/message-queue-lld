package com.lldpractice.messagequeue.topic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.lldpractice.messagequeue.message.Message;

public class InMemoryTopic extends Topic {

    private List<Message> queue;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public InMemoryTopic(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.queue = new ArrayList<>();
    }

    @Override
    public int getCurrentOffset() {
        return this.queue.size();
    }

    @Override
    public void appendMessage(Message message) {
        writeLock.lock();
        try {
            this.queue.add(message);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<Message> getMessages(int fromOffset, int toOffset) {
        if (fromOffset >= toOffset || toOffset > this.getCurrentOffset())
            return Collections.emptyList();
        List<Message> messages = new ArrayList<>();
        readLock.lock();
        try {
            messages = this.queue.subList(fromOffset, toOffset);
        } finally {
            readLock.unlock();
        }
        return messages;
    }

}
