package com.windmeal.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatRoomListResponse {
    private List<ChatRoomSpecResponse> chatRoomSpecResponseList;

    private ChatRoomListResponse(List<ChatRoomSpecResponse> list) {
        this.chatRoomSpecResponseList = list;
    }

    public static ChatRoomListResponse of(List<ChatRoomSpecResponse> list) {
        return new ChatRoomListResponse(list);
    }
}
