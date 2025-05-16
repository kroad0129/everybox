package com.everybox.everybox.repository;

import com.everybox.everybox.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByReceiverId(Long receiverId);
    List<ChatRoom> findBySenderId(Long senderId);
    ChatRoom findBySenderIdAndReceiverIdAndPostId(Long senderId, Long receiverId, Long postId);

}
