package com.bg.imagehoster.kafka;

import com.bg.imagehoster.controller.service.ImageCreateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageProducer {
    private static final String TOPIC = "image-hosting-firebase-uploaded";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public ImageProducer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public void sendMessage(ImageCreateResponse imageListDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(imageListDTO);
        ImageCreateResponse imageResponse = objectMapper.readValue(message, ImageCreateResponse.class);
        System.out.println("Message to Kafka:" + message);
        kafkaTemplate.send(TOPIC, message);
    }
}
