package com.bg.imagehoster.controller;

import com.bg.imagehoster.model.entity.ImageDTO;
import com.bg.imagehoster.service.ImageCreateResponse;
import com.bg.imagehoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    ImageService imageService;

    @PostMapping
    @RequestMapping("/")
    public ImageCreateResponse createimage(@RequestBody ImageDTO image) throws ExecutionException, InterruptedException, IOException {
        return imageService.createImage(image);
    }


}