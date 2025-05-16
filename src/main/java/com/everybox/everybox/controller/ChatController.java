package com.everybox.everybox.controller;

import com.everybox.everybox.domain.ChatRoom;
import com.everybox.everybox.dto.ChatRoomRequestDto;
import com.everybox.everybox.dto.ChatRoomResponseDto;
import com.everybox.everybox.dto.MessageDto;
import com.everybox.everybox.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;

    // 채팅방 생성(중복 방지)
    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @RequestBody ChatRoomRequestDto req,
            Authentication authentication
    ) {
        Long senderId;
        if (authentication.getPrincipal() instanceof String principalStr) {
            senderId = Long.valueOf(principalStr);
        } else if (authentication.getPrincipal() instanceof com.everybox.everybox.security.JwtAuthentication jwtAuth) {
            senderId = jwtAuth.getUserId();
        } else {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
        ChatRoom chatRoom = chatService.createOrGetChatRoom(senderId, req.getReceiverId(), req.getPostId());
        return ResponseEntity.ok(ChatRoomResponseDto.fromEntity(chatRoom));
    }

    // 채팅방 목록(내가 sender/receiver 모두)
    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms(Authentication authentication) {
        Long userId;
        if (authentication.getPrincipal() instanceof String principalStr) {
            userId = Long.valueOf(principalStr);
        } else if (authentication.getPrincipal() instanceof com.everybox.everybox.security.JwtAuthentication jwtAuth) {
            userId = jwtAuth.getUserId();
        } else {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
        List<ChatRoom> rooms = chatService.getChatRoomsByUserId(userId);
        List<ChatRoomResponseDto> dtos = rooms.stream()
                .map(ChatRoomResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/chatrooms/{chatRoomId}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable Long chatRoomId,
                                                        Authentication authentication) {
        Long userId;
        if (authentication.getPrincipal() instanceof String principalStr) {
            userId = Long.valueOf(principalStr);
        } else if (authentication.getPrincipal() instanceof com.everybox.everybox.security.JwtAuthentication jwtAuth) {
            userId = jwtAuth.getUserId();
        } else {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }

        // 메시지 목록을 서비스에서 조회
        List<MessageDto> messages = chatService.getMessagesByChatRoomId(chatRoomId, userId);
        return ResponseEntity.ok(messages);
    }
}
