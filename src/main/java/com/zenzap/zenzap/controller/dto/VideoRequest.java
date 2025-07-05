package com.zenzap.zenzap.controller.dto;

import lombok.Data;

@Data
public class VideoRequest {
    private String youtubeUrl;
    private String title;
    private String description;
    private Integer durationInSeconds;
    private Long categoryId;
}

