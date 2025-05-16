package com.everybox.everybox.dto;

import com.everybox.everybox.domain.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MessageDto {
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String content;
    private String createdAt;

    public static MessageDto fromEntity(Message message) {
        MessageDto dto = new MessageDto();
        dto.setChatRoomId(message.getChatRoom().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getNickname());
        dto.setContent(message.getContent());

        if (message.getCreatedAt() != null) {
            dto.setCreatedAt(message.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            dto.setCreatedAt("");
        }

        return dto;
    }
}
