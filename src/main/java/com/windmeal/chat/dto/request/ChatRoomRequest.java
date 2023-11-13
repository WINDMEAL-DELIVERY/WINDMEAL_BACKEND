package com.windmeal.chat.dto.request;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.member.domain.Member;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {

    private Long ownerId;
    private Long guestId;

    public static ChatRoomRequest of(Long ownerId, Long guestId) {
        return ChatRoomRequest.builder()
                .ownerId(ownerId)
                .guestId(guestId)
                .build();
    }

    public ChatRoom toEntity(Member owner, Member guest) {
        return ChatRoom.builder()
                .owner(owner)
                .guest(guest)
                .build();
    }
}
