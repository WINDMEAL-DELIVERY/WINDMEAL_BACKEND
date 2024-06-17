package com.windmeal.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ChatRoomExistsResponse {
    private final String chatroomId;
    private final String opponentAlarmToken;

    public ChatRoomExistsResponse(String chatroomId, String opponentAlarmToken) {
        this.chatroomId = chatroomId;
        this.opponentAlarmToken = opponentAlarmToken;
    }

    public static ChatRoomExistsResponse nullInstance() {
        return new ChatRoomExistsResponse(null, null);
    }
}
