package com.windmeal.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDeleteRequest {
    private Long roomId;

    public static ChatRoomDeleteRequest of(Long roomId) {
        return ChatRoomDeleteRequest.builder()
                .roomId(roomId)
                .build();
    }
}
