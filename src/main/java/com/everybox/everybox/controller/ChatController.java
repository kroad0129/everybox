package com.everybox.everybox.controller;

import com.everybox.everybox.domain.ChatRoom;
import com.everybox.everybox.domain.Message;
import com.everybox.everybox.service.ChatService;
import com.everybox.everybox.security.JwtAuthentication;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoom createChatRoom(@RequestBody ChatRoomRequest request, Authentication authentication) {
        Long senderId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return chatService.createChatRoom(senderId, request.getPostId());
    }

    @PostMapping("/{chatRoomId}/messages")
    public Message sendMessage(@PathVariable Long chatRoomId, @RequestBody MessageRequest request) {
        return chatService.sendMessage(chatRoomId, request.getSenderId(), request.getContent());
    }

    @GetMapping("/{chatRoomId}/messages")
    public List<Message> getMessages(@PathVariable Long chatRoomId) {
        return chatService.getMessages(chatRoomId);
    }

    @Data
    public static class ChatRoomRequest {
        private Long postId; // ✅ senderId 제거
    }

    @Data
    public static class MessageRequest {
        private Long senderId;
        private String content;
    }

    @GetMapping("/sent")
    public List<ChatRoom> getSentChatRooms(@RequestParam Long userId) {
        return chatService.getSentChatRooms(userId);
    }

    @GetMapping("/received")
    public List<ChatRoom> getReceivedChatRooms(@RequestParam Long userId) {
        return chatService.getReceivedChatRooms(userId);
    }
}