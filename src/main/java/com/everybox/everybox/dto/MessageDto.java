package com.everybox.everybox.dto;

import com.everybox.everybox.domain.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageDto {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
