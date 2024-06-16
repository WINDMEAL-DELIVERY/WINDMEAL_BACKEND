package com.windmeal.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ChatRoomExistsResponse {
    private final String chatroomId;

    public ChatRoomExistsResponse(String chatroomId) {
        this.chatroomId = chatroomId;
    }
}
