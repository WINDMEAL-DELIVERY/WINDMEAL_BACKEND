package com.windmeal.chat.controller;

import com.windmeal.chat.dto.response.ChatMessageResponse;
import com.windmeal.chat.service.ChatMessageService;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

  private final ChatMessageService chatMessageService;


  @Operation(summary = "채팅방의 메시지 조회", description = "전달한 채팅방의 메시자를 페이지네이션으로 조회합니다")
  @ApiResponses({

  })
  @GetMapping("/{chatroomId}")
  public ResultDataResponseDTO<ChatMessageResponse> messageList(@PathVariable Long chatroomId,
      Pageable pageable) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    ChatMessageResponse chatMessages = chatMessageService.findChatMessages(chatroomId,
        currentMemberId, pageable);
    return ResultDataResponseDTO.of(chatMessages);
  }

}
