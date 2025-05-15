package com.everybox.everybox.controller;

import com.everybox.everybox.dto.ChatMessageDto;
import com.everybox.everybox.dto.MessageDto;
import com.everybox.everybox.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId, ChatMessageDto messageDto) {
        // 메시지 저장 및 송신
        MessageDto saved = MessageDto.from(
                chatService.sendMessage(chatRoomId, messageDto.getSenderId(), messageDto.getContent())
        );
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, saved);
    }
}
