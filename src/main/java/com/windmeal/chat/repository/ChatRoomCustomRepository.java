package com.windmeal.chat.repository;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.chat.dto.response.ChatRoomListResponse;

import java.util.List;

public interface ChatRoomCustomRepository {

    List<ChatRoom> findChatRoomsByMemberIdIn(Long CurrentMemberId);
}
