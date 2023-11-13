package com.windmeal.chat.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatRoomResponse {
    private Long roomId;
    private Long ownerId;
    private Long guestId;

    public static ChatRoomResponse of(Long roomId, Long ownerId, Long guestId) {
        return ChatRoomResponse.builder()
                .roomId(roomId)
                .ownerId(ownerId)
                .guestId(guestId)
                .build();
    }
}
