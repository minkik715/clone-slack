package com.webproject.chatservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class ChatRoomRequestDto {

    private String chatRoomName;
    private String chatRoomImg;
    private Set<String> category;
    private Long userId;

}
