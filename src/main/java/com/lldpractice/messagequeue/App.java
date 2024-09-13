package com.lldpractice.messagequeue;

import com.lldpractice.messagequeue.consumer.ConsumerProperties;
import com.lldpractice.messagequeue.message.Message;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import com.lldpractice.messagequeue.consumer.Consumer;
import com.lldpractice.messagequeue.producer.Producer;
import com.lldpractice.messagequeue.producer.ProducerProperties;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Broker broker = new Broker();
        String topicName = "my-topic";
        Producer producer = Producer.builder()
                .properties(ProducerProperties.builder().name("producer-1").topicName(topicName).broker(broker).build())
                .build();
        Consumer consumer1 = Consumer.builder()
                .properties(ConsumerProperties.builder().name("consumer-1").topicName(topicName).broker(broker).build())
                .build();
        Consumer consumer2 = Consumer.builder()
                .properties(ConsumerProperties.builder().name("consumer-2").topicName(topicName).broker(broker).build())
                .build();

        CompletableFuture.runAsync(() -> {
            pollFromConsumer(consumer1, 1);
        });

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(20 * 1000);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
            pollFromConsumer(consumer2, 5);
        });
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.printf("Enter message to be sent by %s: ", producer.getName());
                String messageToPublish = scanner.nextLine();
                System.out.printf("producer: %s published: %s\n", producer.getName(), messageToPublish);
                producer.publishMessage(new Message(producer.getId(), messageToPublish));
            }

        } finally {
            scanner.close();
        }
    }

    public static void pollFromConsumer(Consumer consumer, int delayInSeconds) {
        while (true) {
            try {
                List<Message> messages = consumer.pollMessages();
                // consuming messages
                for (Message message : messages) {
                    System.out.printf("Consumer: %s got: %s\n", consumer.getName(), message.getMessage());
                }

                // sending acknowledgement
                if (!messages.isEmpty()) {
                    consumer.acknowledgeMessages(messages.size());
                    System.out.printf("Consumer: %s acknowledged %d messages\n", consumer.getName(), messages.size());
                }
                Thread.sleep(delayInSeconds * 1000);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}
