package com.everybox.everybox.repository;

import com.everybox.everybox.domain.Message;
import com.everybox.everybox.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoom(ChatRoom chatRoom);
}
