package com.webproject.chatservice.repository;

import com.webproject.chatservice.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByOrderByCreatedAtDesc();
    List<ChatRoom> findByCategory(String category);
}
