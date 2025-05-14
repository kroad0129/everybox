package com.everybox.everybox.controller;

import com.everybox.everybox.domain.Message;
import com.everybox.everybox.service.ChatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId, ChatMessageDto messageDto) {
        Message message = chatService.sendMessage(
                chatRoomId,
                messageDto.getSenderId(),
                messageDto.getContent()
        );

        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, message);
    }

    @Data
    public static class ChatMessageDto {
        private Long senderId;
        private String content;
    }
}
