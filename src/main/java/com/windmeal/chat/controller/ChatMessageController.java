package com.windmeal.chat.controller;

import com.windmeal.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

//  @GetMapping("/{chatroomId}")
//  public

}
