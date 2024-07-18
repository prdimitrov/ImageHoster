package com.bg.imagehoster.controller.service;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageCreateResponse {
    private String id;
    private String url;
    private String oldUrl;
}
