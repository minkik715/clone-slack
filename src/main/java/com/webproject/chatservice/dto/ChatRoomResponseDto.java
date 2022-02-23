package com.webproject.chatservice.dto;

import com.webproject.chatservice.models.ChatRoom;
import com.webproject.chatservice.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ToString
@Getter@Setter
public class ChatRoomResponseDto {

    private Long id;
    private String chatRoomName;
    private String chatRoomImg;
    private Set<String> category;
    private User user;

    public ChatRoomResponseDto(ChatRoom chatRoom, User user) {
        this.id = chatRoom.getId();
        this.chatRoomName = chatRoom.getChatRoomName();
        this.chatRoomImg = chatRoom.getChatRoomImg();
        this.category = chatRoom.getCategory();
        this.user = user;
    }
}
