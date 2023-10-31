package com.windmeal.chat.controller;

import com.windmeal.chat.dto.request.ChatRoomRequestDTO;
import com.windmeal.chat.dto.response.ChatRoomResponseDTO;
import com.windmeal.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomResponseDTO createChatRoom(@RequestBody ChatRoomRequestDTO requestDTO) {
        return chatRoomService.createChatRoom(requestDTO);
    }
}
