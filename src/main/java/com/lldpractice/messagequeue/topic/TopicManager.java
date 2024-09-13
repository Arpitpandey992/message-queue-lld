package com.lldpractice.messagequeue.topic;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.lldpractice.messagequeue.message.Message;

public class TopicManager {
    Map<String, Topic> topics;

    public void appendToTopic(String topicName, Message message) {
        if (!topics.containsKey(topicName)) {
            createTopic(topicName);
        }
        topics.get(topicName).appendMessage(message);
    }

    public List<Message> getAllMessagesFrom(String topicName, int offset) {
        if (!this.topics.containsKey(topicName))
            return Collections.emptyList();
        Topic topic = this.topics.get(topicName);
        return topic.getMessages(offset, topic.getCurrentOffset());
    }

    public Topic getTopic(String topicName) {
        return topics.getOrDefault(topicName, null);
    }

    private void createTopic(String topicName) {
        topics.put(topicName, new InMemoryTopic(topicName));
    }
}
