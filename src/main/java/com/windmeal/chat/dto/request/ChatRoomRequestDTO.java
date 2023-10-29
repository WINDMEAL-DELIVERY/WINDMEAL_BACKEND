package com.windmeal.chat.dto.request;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.member.domain.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatRoomRequestDTO {

    private Long ownerId;
    private Long guestId;

    public static ChatRoomRequestDTO of(Long ownerId, Long guestId) {
        return ChatRoomRequestDTO.builder()
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
