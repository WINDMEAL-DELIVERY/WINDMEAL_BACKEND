package com.windmeal.chat.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
    private Long roomId;
    private Long ownerId;
    private Long guestId;
    private Long orderId;

    public static ChatRoomResponse of(Long roomId, Long ownerId, Long guestId, Long orderId) {
        return ChatRoomResponse.builder()
                .roomId(roomId)
                .ownerId(ownerId)
                .guestId(guestId)
                .orderId(orderId)
                .build();
    }
}
