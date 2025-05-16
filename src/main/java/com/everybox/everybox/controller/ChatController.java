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

    private Long getUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof String str) {
            return Long.valueOf(str);
        } else if (principal instanceof com.everybox.everybox.security.JwtAuthentication jwtAuth) {
            return jwtAuth.getUserId();
        } else {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
    }

    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @RequestBody ChatRoomRequestDto req,
            Authentication authentication
    ) {
        Long senderId = getUserId(authentication);
        ChatRoom chatRoom = chatService.createOrGetChatRoom(senderId, req.getReceiverId(), req.getPostId());
        return ResponseEntity.ok(ChatRoomResponseDto.fromEntity(chatRoom));
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<ChatRoom> rooms = chatService.getChatRoomsByUserId(userId);
        List<ChatRoomResponseDto> dtos = rooms.stream()
                .map(ChatRoomResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/chatrooms/{chatRoomId}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(
            @PathVariable Long chatRoomId,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        List<MessageDto> messages = chatService.getMessagesByChatRoomId(chatRoomId, userId);
        return ResponseEntity.ok(messages);
    }
}
