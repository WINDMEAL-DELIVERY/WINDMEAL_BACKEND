//package com.windmeal.chat.repository;
//
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.windmeal.chat.dto.response.ChatRoomListResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.domain.SliceImpl;
//
//import java.util.List;
//
//import static com.windmeal.chat.domain.QChatRoom.chatRoom;
//
//@RequiredArgsConstructor
//public class ChatRoomRepositoryImpl implements ChatRoomCustomRepository {
//
//    private final JPAQueryFactory jpaQueryFactory;
//    @Override
//    public Slice<ChatRoomListResponse.ChatRoomSpecResponse> findChatRoomsByMemberId(Long currentMemberId, Pageable pageable) {
//        // 자신이 삭제하지 않은 채팅방에 대해서만 조회한다.
//        List<ChatRoomListResponse.ChatRoomSpecResponse> list = jpaQueryFactory.select(Projections.constructor(
//                        ChatRoomListResponse.ChatRoomSpecResponse.class,
//                        chatRoom.id,
//                        chatRoom.owner.id,
//                        chatRoom.guest.id,
//                        chatRoom.isDeletedByOwner,
//                        chatRoom.isDeletedByGuest
//                ))
//                .from(chatRoom)
//                .where((chatRoom.guest.id.eq(currentMemberId).and(chatRoom.isDeletedByGuest.eq(false)))
//                        .or((chatRoom.owner.id.eq(currentMemberId)).and(chatRoom.isDeletedByOwner.eq(false))))
//                .limit(pageable.getPageSize() + 1)
//                .fetch();
//
//        boolean hasNext = false;
//        if(list.size() > pageable.getPageSize()) {
//            list.remove(pageable.getPageSize());
//            hasNext = true;
//        }
//        return new SliceImpl<>(list, pageable, hasNext);
//    }
//
//}
