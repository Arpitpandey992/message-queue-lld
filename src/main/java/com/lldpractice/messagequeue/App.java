package com.lldpractice.messagequeue;

import com.lldpractice.messagequeue.consumer.ConsumerProperties;
import com.lldpractice.messagequeue.message.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
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
        String outputDirectory = "logs";
        File directory = new File(outputDirectory);
        if (!directory.exists())
            directory.mkdirs();
        String outputFilePath = Paths.get(outputDirectory, consumer.getName() + ".log").toString();
        File logFile = new File(outputFilePath);

        if (logFile.exists()) {
            if (!logFile.delete()) {
                System.err.println("Failed to delete the log file: " + outputFilePath);
                return;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            while (true) {
                try {
                    List<Message> messages = consumer.pollMessages();
                    // consuming messages
                    if (!messages.isEmpty()) {
                        writer.write("------------------------------------------\n");
                        for (Message message : messages) {
                            writer.write(String.format("Consumer: %s got: %s\n",
                                    consumer.getName(),
                                    message.getMessage()));
                        }

                        // sending acknowledgement
                        consumer.acknowledgeMessages(messages.size());
                        writer.write(String.format(
                                "------------------------------------------\nConsumer: %s acknowledged %d messages\n",
                                consumer.getName(),
                                messages.size()));
                    }
                    writer.flush();
                    Thread.sleep(delayInSeconds * 1000);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
