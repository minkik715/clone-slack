package com.webproject.chatservice.dto;

import com.webproject.chatservice.models.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private String roomId;
    private Long userId;
    private String sender;
    private String message;
    private String createdAt;

}
