package com.windmeal.chat.dto.response;

import com.windmeal.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomSpecResponse {
    private final Long chatRoomId;
    private final Long ownerId;
    private final Long guestId;

    public ChatRoomSpecResponse(Long chatRoomId, Long ownerId, Long guestId) {
        this.chatRoomId = chatRoomId;
        this.ownerId = ownerId;
        this.guestId = guestId;
    }

    public static ChatRoomSpecResponse of(ChatRoom chatRoom) {
        return new ChatRoomSpecResponse(chatRoom.getId(), chatRoom.getOwner().getId(), chatRoom.getGuest().getId());
    }
}
