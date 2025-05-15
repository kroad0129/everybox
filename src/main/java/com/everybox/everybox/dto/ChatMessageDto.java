package com.everybox.everybox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private Long senderId;
    private String content;
}
