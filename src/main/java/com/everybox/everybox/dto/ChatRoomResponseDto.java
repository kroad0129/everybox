package com.everybox.everybox.dto;

import com.everybox.everybox.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long id;
    private Long postId;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createdAt;

    public static ChatRoomResponseDto from(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .postId(chatRoom.getPost().getId())
                .senderId(chatRoom.getSender().getId())
                .receiverId(chatRoom.getReceiver().getId())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
