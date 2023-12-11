package com.windmeal.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "채팅방 단일 조회")
public class ChatRoomResponse {
    private String roomId;
    private Long ownerId;
    private Long guestId;
    private Long orderId;

    public static ChatRoomResponse of(String roomId, Long ownerId, Long guestId, Long orderId) {
        return ChatRoomResponse.builder()
                .roomId(roomId)
                .ownerId(ownerId)
                .guestId(guestId)
                .orderId(orderId)
                .build();
    }
}
