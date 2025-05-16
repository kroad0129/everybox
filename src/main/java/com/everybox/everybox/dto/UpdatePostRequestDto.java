package com.everybox.everybox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostRequestDto {
    private String title;
    private String details;
    private String location;
    private Integer quantity;
    private String imageUrl;
    private Boolean isClosed;
}
