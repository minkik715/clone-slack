package com.webproject.chatservice.models;

import com.webproject.chatservice.dto.ChatRoomRequestDto;
import com.webproject.chatservice.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends Timestamped {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column
    private String chatRoomName;

    @Column
    private String chatRoomImg;

    @ElementCollection
    private Set<String> category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<User> userList = new ArrayList<>();


    public ChatRoom(ChatRoomRequestDto requestDto, UserService userService) {
        this.chatRoomName = requestDto.getChatRoomName();
        this.chatRoomImg = requestDto.getChatRoomImg();
        this.category = requestDto.getCategory();
        this.userList.add(userService.findById(requestDto.getUserId()));
    }

}
