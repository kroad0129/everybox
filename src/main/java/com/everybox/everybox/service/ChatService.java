package com.everybox.everybox.service;

import com.everybox.everybox.domain.*;
import com.everybox.everybox.dto.ChatMessageDto;
import com.everybox.everybox.dto.MessageDto;
import com.everybox.everybox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MessageRepository messageRepository;

    // 1:1 쌍당 1개만!
    public ChatRoom createOrGetChatRoom(Long senderId, Long receiverId, Long postId) {
        ChatRoom existingRoom = chatRoomRepository.findBySenderIdAndReceiverIdAndPostId(senderId, receiverId, postId);
        if (existingRoom != null) return existingRoom;
        // 반대 쌍도 체크(서로 역할 바뀔 수 있으니까)
        ChatRoom reverseRoom = chatRoomRepository.findBySenderIdAndReceiverIdAndPostId(receiverId, senderId, postId);
        if (reverseRoom != null) return reverseRoom;
        // 없으면 새로 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .sender(userRepository.findById(senderId).orElseThrow())
                .receiver(userRepository.findById(receiverId).orElseThrow())
                .post(postRepository.findById(postId).orElseThrow())
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        List<ChatRoom> sent = chatRoomRepository.findBySenderId(userId);
        List<ChatRoom> received = chatRoomRepository.findByReceiverId(userId);
        sent.addAll(received);
        return sent;
    }

    // 메시지 저장
    public Message saveMessage(ChatMessageDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow();
        User sender = userRepository.findById(dto.getSenderId()).orElseThrow();
        Message message = Message.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(dto.getContent())
                .build();
        return messageRepository.save(message);
    }
    public List<MessageDto> getMessagesByChatRoomId(Long chatRoomId, Long userId) {
        // 인증 유저가 채팅방 참여자인지 체크 필요(보안상)
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        if (!chatRoom.getSender().getId().equals(userId) && !chatRoom.getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException("채팅방에 참여하지 않은 사용자입니다.");
        }

        List<Message> messages = messageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream().map(MessageDto::fromEntity).collect(Collectors.toList());
    }
}
