package com.windmeal.chat.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatRoomResponseDTO {
    private Long roomId;
    private Long ownerId;
    private Long guestId;

    public static ChatRoomResponseDTO of(Long roomId, Long ownerId, Long guestId) {
        return ChatRoomResponseDTO.builder()
                .roomId(roomId)
                .ownerId(ownerId)
                .guestId(guestId)
                .build();
    }
}
