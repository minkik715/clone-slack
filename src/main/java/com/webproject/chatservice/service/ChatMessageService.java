package com.webproject.chatservice.service;

import com.webproject.chatservice.models.ChatMessage;
import com.webproject.chatservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    // 채팅방에 메시지 발송
    public void sendChatMessage(ChatMessage chatMessage) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage("");
            chatMessage.setSender("");
            log.info("ENTER 데이터 날라갑니다~!");
            //*chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            //chatMessage.setSender("[알림]");*//*
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage("");
            chatMessage.setSender("");
            log.info("QUIT 데이터 날라갑니다~!");
            //*chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            //chatMessage.setSender("[알림]");
        }
        //topic은 chatroom이다
        //conver
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    public void save(ChatMessage chatMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatMessage.getType());
        message.setRoomId(chatMessage.getRoomId());
        message.setUser(userService.findById(chatMessage.getUserId()));
        message.setUserId(chatMessage.getUserId());
        message.setSender(chatMessage.getSender());
        message.setMessage(chatMessage.getMessage());
        message.setCreatedAt(chatMessage.getCreatedAt());
        chatMessageRepository.save(message);
    }

    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 150);
        return chatMessageRepository.findByRoomId(roomId, pageable);
    }

}
