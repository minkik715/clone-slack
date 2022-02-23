package com.webproject.chatservice.dto;

import com.webproject.chatservice.models.ChatRoom;
import com.webproject.chatservice.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ChatRoomListDto {

    private Long id;
    private String chatRoomName;
    private String chatRoomImg;
    private Set<String> category;
    private List<User> userList = new ArrayList<>();
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ChatRoomListDto(ChatRoom chatRoom, User user) {
        this.id = chatRoom.getId();
        this.chatRoomName = chatRoom.getChatRoomName();
        this.chatRoomImg = chatRoom.getChatRoomImg();
        this.category = chatRoom.getCategory();
        this.userList = chatRoom.getUserList();
        this.username = user.getUsername();
        this.createdAt = chatRoom.getCreatedAt();
        this.modifiedAt = chatRoom.getModifiedAt();
    }
}
