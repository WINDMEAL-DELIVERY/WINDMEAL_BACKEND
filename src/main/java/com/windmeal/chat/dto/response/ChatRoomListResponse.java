package com.windmeal.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListResponse {
    private List<ChatRoomSpecResponse> chatRoomSpecResponseList;

    public static ChatRoomListResponse of(List<ChatRoomSpecResponse> list) {
        return ChatRoomListResponse.builder()
                .chatRoomSpecResponseList(list)
                .build();
    }
}
