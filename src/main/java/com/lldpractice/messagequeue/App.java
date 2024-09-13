package com.lldpractice.messagequeue;

import com.lldpractice.messagequeue.message.Message;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Message message = Message.builder().message("damn").producerId("p1").build();
        System.out.println(message);
    }
}
