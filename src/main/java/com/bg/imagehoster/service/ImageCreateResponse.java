package com.bg.imagehoster.service;

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
    private String tableName;
}
