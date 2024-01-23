package com.windmeal.chat.dto.request;

import com.windmeal.chat.domain.ChatroomDocument;
import com.windmeal.member.domain.Member;
import com.windmeal.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "주문(게시글)에 대한 채팅방 생성")
public class ChatRoomSaveRequest {

    @Schema(description = "게시글의 ID", example = "3")
    private Long orderId;

    public static ChatRoomSaveRequest of(Long orderId) {
        return ChatRoomSaveRequest.builder()
                .orderId(orderId)
                .build();
    }


    public ChatroomDocument toDocument(Member owner, Member guest, Order order, String ownerAlarmToken, String guestAlarmToken) {
        return ChatroomDocument.builder()
            .ownerId(owner.getId())
            .guestId(guest.getId())
            .orderId(order.getId())
            .ownerEmail(owner.getEmail())
            .guestEmail(guest.getEmail())
            .ownerAlarmToken(ownerAlarmToken)
            .guestAlarmToken(guestAlarmToken)
            .ownerNickname(owner.getNickname())
            .guestNickname(guest.getNickname())
            .build();
    }
}
