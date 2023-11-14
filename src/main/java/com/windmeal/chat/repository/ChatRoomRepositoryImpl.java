package com.windmeal.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.windmeal.chat.domain.QChatRoom.chatRoom;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<ChatRoomListResponse.ChatRoomSpecResponse> findChatRoomsByMemberId(Long currentMemberId) {
        // 자신이 삭제하지 않은 채팅방에 대해서만 조회한다.
        return jpaQueryFactory.select(Projections.constructor(
                        ChatRoomListResponse.ChatRoomSpecResponse.class,
                    chatRoom.id,
                    chatRoom.owner.id,
                    chatRoom.guest.id,
                    chatRoom.isDeletedByOwner,
                    chatRoom.isDeletedByGuest
                ))
                .from(chatRoom)
                .where((chatRoom.guest.id.eq(currentMemberId).and(chatRoom.isDeletedByGuest.eq(false)))
                        .or((chatRoom.owner.id.eq(currentMemberId)).and(chatRoom.isDeletedByOwner.eq(false))))
                .fetch();
    }
}
