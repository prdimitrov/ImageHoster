package com.bg.imagehoster;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @KafkaListener(topics = "image-hosting")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group-images: " + message);
    }
}
