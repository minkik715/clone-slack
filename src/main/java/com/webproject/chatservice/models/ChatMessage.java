package com.webproject.chatservice.models;

import com.webproject.chatservice.dto.ChatMessageRequestDto;
import com.webproject.chatservice.service.UserService;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private MessageType type;

    @Column
    private String roomId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_joined")
    private User user;

    // Redis MessageListener 로 뒙소켓을 통해 바로 채팅방에 메시지를 전달해주기 위한 값을 따로 설정해주었다
    @Column
    private Long userId;

    @Column
    private String sender;

    @Column
    private String message;

    @Column
    private String createdAt;

    @Builder
    public ChatMessage(MessageType type, String roomId, Long userId, String sender, String senderEmail, String senderImg, String message, String createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.user = null;
        this.userId = userId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.user = null;
        this.userId = chatMessageRequestDto.getUserId();
        this.sender = chatMessageRequestDto.getSender();
        this.message = chatMessageRequestDto.getMessage();
        this.createdAt = chatMessageRequestDto.getCreatedAt();
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, UserService userService) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.user = userService.findById(chatMessageRequestDto.getUserId());
        this.userId = chatMessageRequestDto.getUserId();
        this.sender = chatMessageRequestDto.getSender();
        this.message = chatMessageRequestDto.getMessage();
        this.createdAt = chatMessageRequestDto.getCreatedAt();
    }
}