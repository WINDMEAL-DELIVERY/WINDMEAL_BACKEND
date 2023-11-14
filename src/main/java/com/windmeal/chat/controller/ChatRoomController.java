package com.windmeal.chat.controller;

import com.windmeal.chat.dto.request.ChatRoomDeleteRequest;
import com.windmeal.chat.dto.request.ChatRoomSaveRequest;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.service.ChatRoomService;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "채팅방", description = "채팅방 관련 API 입니다.")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채팅방 생성 요청", description = "채팅방이 생성됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청자와 접속자의 정보가 다름"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "해당되는 사용자가 존재하지 않음"),
            @ApiResponse(responseCode = "404", description = "주문(게시글)이 존제하지 않음")
    })
    public ResultDataResponseDTO<ChatRoomResponse> createChatRoom(@RequestBody ChatRoomSaveRequest requestDTO) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResultDataResponseDTO.of(chatRoomService.createChatRoom(requestDTO, currentMemberId));
    }

    @GetMapping("/chatroom")
    @Operation(summary = "채팅방 조회 요청", description = "사용자가 속한 채팅방 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResultDataResponseDTO<ChatRoomListResponse> getChatRoomList() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResultDataResponseDTO.of(chatRoomService.getChatRoomsByMemberId(currentMemberId));
    }

    @DeleteMapping
    @Operation(summary = "채팅방 삭제 요청", description = "채팅방을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 요청자가 채팅방에 속해있지 않음"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방")
    })
    public ResultDataResponseDTO deleteChatRoom(@RequestBody ChatRoomDeleteRequest deleteRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        chatRoomService.deleteChatRoom(deleteRequest, currentMemberId);
        return ResultDataResponseDTO.empty();
    }

}
