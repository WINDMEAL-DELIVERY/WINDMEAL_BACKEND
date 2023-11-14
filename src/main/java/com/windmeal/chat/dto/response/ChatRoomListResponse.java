package com.windmeal.chat.dto.response;

import com.windmeal.chat.domain.ChatRoom;
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


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomSpecResponse {
        private Long chatRoomId;
        private Long ownerId;
        private Long guestId;
        private boolean isDeletedByOwner;
        private boolean isDeletedByGuest;
        // TODO order의 한줄 요약, 매칭 여부, 매칭되었다면 현재 상태 등이 추가될 예정
    }
}
