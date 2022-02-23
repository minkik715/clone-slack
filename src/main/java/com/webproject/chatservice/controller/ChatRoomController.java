package com.webproject.chatservice.controller;

import com.webproject.chatservice.dto.ChatRoomListDto;
import com.webproject.chatservice.dto.ChatRoomRequestDto;
import com.webproject.chatservice.dto.ChatRoomResponseDto;
import com.webproject.chatservice.dto.InvitationDto;
import com.webproject.chatservice.models.ChatMessage;
import com.webproject.chatservice.models.ChatRoom;
import com.webproject.chatservice.models.UserDetailsImpl;
import com.webproject.chatservice.service.ChatMessageService;
import com.webproject.chatservice.service.ChatRoomService;
import com.webproject.chatservice.utils.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final Uploader uploader;

    @Autowired
    public ChatRoomController(ChatMessageService chatMessageService, ChatRoomService chatRoomService, Uploader uploader) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.uploader = uploader;
    }

    // 채팅방 생성
    @PostMapping("/rooms")
    public ChatRoomResponseDto ChatRoomResponseDto(@RequestBody ChatRoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        log.info("requestDto = {}", requestDto);
        requestDto.setUserId(userDetails.getUser().getId());

        ChatRoomResponseDto chatRoom = chatRoomService.createChatRoom(requestDto);
        log.info("responseDto = {}", chatRoom);
        return chatRoom;
    }

    // 전체 채팅방 목록 조회
    @GetMapping("/rooms")
    public List<ChatRoomListDto> getAllChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getAllChatRooms(userDetails.getUser());

    }


    //유저 초대하기
    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody InvitationDto invitationDto){
        return chatRoomService.inviteUser(invitationDto);
    }

    /*@PostMapping("/kickout")
    public ResponseEntity<?> kickOutUser(@RequestBody InvitationDto invitationDto){
        return chatRoomService.kickOutUser(invitationDto);
    }*/

    //채팅방 나가기
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<?> outChatRoom(@PathVariable Long roomId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatRoomService.outChatRoom(roomId,userDetails.getUser());
    }

    // 채팅팅방 카테고리별 조회
    @GetMapping("/rooms/search/{category}")
    public List<ChatRoom> getChatRoomsByCategory(@PathVariable String category, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getAllChatRoomsByCategory(category, userDetails.getUser());
    }

    // 채팅방 상세 조회
    @GetMapping("/rooms/{roomId}")
    public ChatRoomResponseDto getEachChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getEachChatRoom(roomId,userDetails.getUser());
    }

    // 채팅방 내 메시지 전체 조회
    @GetMapping("/rooms/{roomId}/messages")
    public Page<ChatMessage> getEachChatRoomMessages(@PathVariable String roomId, @PageableDefault Pageable pageable) {
        return chatMessageService.getChatMessageByRoomId(roomId, pageable);
    }

}
