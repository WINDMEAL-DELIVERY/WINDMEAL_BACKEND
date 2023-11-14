package com.windmeal.chat.repository;

import com.windmeal.chat.dto.response.ChatRoomSpecResponse;

import java.util.List;

public interface ChatRoomCustomRepository {

    List<ChatRoomSpecResponse> findChatRoomsByMemberId(Long CurrentMemberId);
}
