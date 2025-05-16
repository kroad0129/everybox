package com.everybox.everybox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSendRequestDto {
    private Long chatRoomId;
    private Long senderId;
    private String content;
}
