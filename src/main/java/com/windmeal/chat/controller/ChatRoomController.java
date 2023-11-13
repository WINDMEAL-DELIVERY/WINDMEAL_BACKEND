package com.windmeal.chat.controller;

import com.windmeal.chat.dto.request.ChatRoomRequestDTO;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.service.ChatRoomService;
import com.windmeal.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomResponse createChatRoom(@RequestBody ChatRoomRequestDTO requestDTO) {
        return chatRoomService.createChatRoom(requestDTO);
    }

    @GetMapping("/chatroom")
    public ChatRoomListResponse getChatRoomList() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return chatRoomService.getChatRoomsByMemberId(currentMemberId);
    }
}
