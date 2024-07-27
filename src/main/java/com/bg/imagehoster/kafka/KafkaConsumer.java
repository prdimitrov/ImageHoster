package com.bg.imagehoster.kafka;

import com.bg.imagehoster.model.dto.ImageListDTO;
import com.bg.imagehoster.model.entity.ImageDTO;
import com.bg.imagehoster.service.ImageCreateResponse;
import com.bg.imagehoster.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaConsumer {
    private final ImageService imageService;
    private final ImageProducer imageProducer;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(ImageService imageService, ImageProducer imageProducer, ObjectMapper objectMapper) {
        this.imageService = imageService;
        this.imageProducer = imageProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "image-hosting")
    public void consume(String message) throws IOException, ExecutionException, InterruptedException {

        ImageListDTO imageListDTO = objectMapper.readValue(message, ImageListDTO.class);

        log.info("Received message: {}", imageListDTO.getImageUrls());
        log.info("Received message: {}", imageListDTO.getTableName());

        for (String imageUrl : imageListDTO.getImageUrls()) {
            ImageDTO temp = new ImageDTO();
            temp.setImageUrl(imageUrl);
            ImageCreateResponse response = imageService.createImage(temp);
            response.setTableName(imageListDTO.getTableName());
            imageProducer.sendMessage(response);
            log.info(response.getOldUrl());
            log.info("------------------------------");
            log.info(response.getUrl());
            log.info(System.lineSeparator());
        }
    }
}
