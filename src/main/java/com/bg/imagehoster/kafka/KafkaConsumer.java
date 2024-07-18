package com.bg.imagehoster.kafka;

import com.bg.imagehoster.controller.entity.ImageDTO;
import com.bg.imagehoster.controller.service.ImageCreateResponse;
import com.bg.imagehoster.controller.service.ImageService;
import com.bg.imagehoster.kafka.dto.ImageListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaConsumer {
    private final ImageService imageService;
    private final ImageProducer imageProducer;

    public KafkaConsumer(ImageService imageService, ImageProducer imageProducer) {
        this.imageService = imageService;
        this.imageProducer = imageProducer;
    }

    @KafkaListener(topics = "image-hosting")
    public void consume(String message) throws IOException, ExecutionException, InterruptedException {

            List<String> imageUrls = Arrays.asList(message.replace("[", "").replace("]", "").split(","));
            ImageListDTO imageListDTO = new ImageListDTO(imageUrls);
            System.out.println("Received Message: " + imageListDTO.getImageUrls());
            // call firebase foreach image
            for (String imageUrl : imageUrls) {
                ImageDTO temp = new ImageDTO();
                temp.setImageUrl(imageUrl);
                ImageCreateResponse response =  imageService.createImage(temp);
                imageProducer.sendMessage(response);
                log.info(response.getOldUrl());
                log.info("------------------------------");
                log.info(response.getUrl());
                log.info(System.lineSeparator());
            }

    }
}
