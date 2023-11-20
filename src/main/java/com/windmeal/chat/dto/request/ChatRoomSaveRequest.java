package com.windmeal.chat.dto.request;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.member.domain.Member;
import com.windmeal.order.domain.Order;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomSaveRequest {

    private Long ownerId;
    private Long guestId;
    private Long orderId;

    public static ChatRoomSaveRequest of(Long ownerId, Long guestId, Long orderId) {
        return ChatRoomSaveRequest.builder()
                .ownerId(ownerId)
                .guestId(guestId)
                .orderId(orderId)
                .build();
    }

    public ChatRoom toEntity(Member owner, Member guest, Order order) {
        return ChatRoom.builder()
                .owner(owner)
                .guest(guest)
                .order(order)
                .build();
    }
}
