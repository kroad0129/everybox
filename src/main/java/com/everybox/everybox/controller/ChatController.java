package com.everybox.everybox.controller;

import com.everybox.everybox.dto.ChatRoomRequestDto;
import com.everybox.everybox.dto.ChatRoomResponseDto;
import com.everybox.everybox.dto.MessageDto;
import com.everybox.everybox.dto.MessageRequestDto;
import com.everybox.everybox.security.JwtAuthentication;
import com.everybox.everybox.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @RequestBody ChatRoomRequestDto request,
            Authentication authentication) {
        Long senderId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(ChatRoomResponseDto.from(chatService.createChatRoom(senderId, request.getPostId())));
    }

    @PostMapping("/{chatRoomId}/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable Long chatRoomId,
            @RequestBody MessageRequestDto request,
            Authentication authentication) {
        Long senderId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(MessageDto.from(chatService.sendMessage(chatRoomId, senderId, request.getContent())));
    }

    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(
                chatService.getMessages(chatRoomId)
                        .stream()
                        .map(MessageDto::from)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/sent")
    public ResponseEntity<List<ChatRoomResponseDto>> getSentChatRooms(Authentication authentication) {
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(
                chatService.getSentChatRooms(userId)
                        .stream()
                        .map(ChatRoomResponseDto::from)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/received")
    public ResponseEntity<List<ChatRoomResponseDto>> getReceivedChatRooms(Authentication authentication) {
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(
                chatService.getReceivedChatRooms(userId)
                        .stream()
                        .map(ChatRoomResponseDto::from)
                        .collect(Collectors.toList())
        );
    }
}
