package com.everybox.everybox.dto;

import com.everybox.everybox.domain.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private Long postId;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private LocalDateTime createdAt;

    public static ChatRoomResponseDto fromEntity(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .postId(chatRoom.getPost().getId())
                .senderId(chatRoom.getSender().getId())
                .senderName(chatRoom.getSender().getNickname())
                .receiverId(chatRoom.getReceiver().getId())
                .receiverName(chatRoom.getReceiver().getNickname())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
