package com.windmeal.chat.dto.request;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.member.domain.Member;
import lombok.*;

@Getter
@Builder
public class ChatRoomRequestDTO {

    private final Long ownerId;
    private final Long guestId;

    private ChatRoomRequestDTO(Long ownerId, Long guestId) {
        this.ownerId = ownerId;
        this.guestId = guestId;
    }

    public static ChatRoomRequestDTO of(Long ownerId, Long guestId) {
        return new ChatRoomRequestDTO(ownerId, guestId);
    }

    public ChatRoom toEntity(Member owner, Member guest) {
        return ChatRoom.builder()
                .owner(owner)
                .guest(guest)
                .build();
    }
}
