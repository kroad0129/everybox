package com.everybox.everybox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequestDto {
    private String title;
    private String details;
    private String location;
    private int quantity;
    private String imageUrl;
}
