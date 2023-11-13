package com.windmeal.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.chat.domain.QChatRoom;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.windmeal.chat.domain.QChatRoom.*;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<ChatRoom> findChatRoomsByMemberIdIn(Long currentMemberId) {
        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.guest.id.eq(currentMemberId).or(chatRoom.owner.id.eq(currentMemberId)))
                .fetch();
    }
}
