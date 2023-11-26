package com.windmeal.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "주문(게시글)에 대한 채팅방 삭제")
public class ChatRoomDeleteRequest {

    @Schema(title = "삭제할 채팅방의 ID")
    private Long roomId;

    public static ChatRoomDeleteRequest of(Long roomId) {
        return ChatRoomDeleteRequest.builder()
                .roomId(roomId)
                .build();
    }
}
