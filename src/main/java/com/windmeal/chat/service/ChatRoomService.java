package com.windmeal.chat.service;

import com.windmeal.chat.domain.ChatroomDocument;
import com.windmeal.chat.dto.request.ChatRoomDeleteRequest;
import com.windmeal.chat.dto.request.ChatRoomSaveRequest;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.exception.ChatRoomNotFoundException;
import com.windmeal.chat.exception.InvalidRequesterException;
import com.windmeal.chat.repository.ChatroomDocumentRepository;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatroomDocumentRepository chatroomDocumentRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    /**
     * 채팅방 생성을 관장하는 메서드
     * @param requestDTO
     * @return ChatRoomResponse
     */
    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomSaveRequest requestDTO, Long currentMemberId) {

        Order order = orderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "주문이 존재하지 않습니다."));
        Member owner = memberRepository.findById(order.getOrderer_id())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "주문 작성자를 찾을 수 없습니다."));
        Member guest = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        // 주문이 진행중이면 채팅방을 생성할 수 없다.
        if(!order.getOrderStatus().equals(OrderStatus.ORDERED)) {
            throw new OrderAlreadyMatchedException(ErrorCode.BAD_REQUEST, "이미 매칭된 주문입니다.");
        }
//        ChatRoom toEntity = requestDTO.toEntity(owner, guest, order);
//        ChatRoom chatRoom = chatRoomRepository.save(toEntity);
        ChatroomDocument chatRoom = chatroomDocumentRepository.save(
            requestDTO.toDocument(owner, guest, order));
        return ChatRoomResponse.of(chatRoom.getId(), chatRoom.getOwnerId(), chatRoom.getGuestId(),
            chatRoom.getOrderId());
    }

//    /**
//     * 채팅방 조회를 관장하는 메서드
//     * 현재 사용자가 속한 모든 채팅방을 조회하고, 채팅방의 아이디와 속한 사용자들의 아이디를 리스트로 반환한다
//     * 사용자가 처음 접속하면 자신이 속한 채팅방을 모두 구독해야 하기 때문에, 채팅방의 아이디 리스트가 필요할 것이고, 이를 위해 호출될 api에 사용된다.
//     * 이전에 나간 채팅방일 경우 해당 로직에서 조회되지 않는다.
//     * TODO 상대방이 채팅방을 나가지 않았을 경우, 상대의 알람 토큰을 함께 전달해주어야 한다.
//     * @param memberId
//     * @return ChatRoomListResponse
//     */
//    public ChatRoomListResponse getChatRoomsByMemberId(Long memberId, Pageable pageable) {
//        Slice<ChatRoomListResponse.ChatRoomSpecResponse> chatRoomSpecResponses = chatRoomRepository.findChatRoomsByMemberId(memberId, pageable);
//        return ChatRoomListResponse.of(chatRoomSpecResponses);
//    }
//
//    /**
//     * 채팅방 삭제를 관장하는 메서드
//     * @param deleteRequest
//     * @param currentMemberId
//     */
//    @Transactional
//    public void deleteChatRoom(ChatRoomDeleteRequest deleteRequest, Long currentMemberId) {
//        // 요청으로 들어온 채팅방에 사용자가 속한지 확인해야 한다.
//        ChatRoom chatRoom = chatRoomRepository.findByIdWithFetchJoin(deleteRequest.getRoomId());
//        // TODO jpql로 작성하면 optional이 안되는 듯 하다. 계속 알아보는 중
//        if(chatRoom == null) {
//            throw new ChatRoomNotFoundException(ErrorCode.NOT_FOUND, "채팅방이 존재하지 않습니다.");
//        }
//        Long ownerId = chatRoom.getOwner().getId();
//        Long guestId = chatRoom.getGuest().getId();
//
//        // 요청자를 검증하고, 요청자가 누구인가에 따라 필드를 업데이트 해준다.
//        if(!ownerId.equals(currentMemberId) && !guestId.equals(currentMemberId)) {
//            throw new InvalidRequesterException(ErrorCode.VALIDATION_ERROR, "요청자가 해당 채팅방에 속해있지 않습니다.");
//        }
//        if(ownerId.equals(currentMemberId)) {
//            chatRoom.updateDeletedByOwner(true);
//        } else {
//            chatRoom.updateDeletedByGuest(true);
//        }
//        // TODO 배달이 완료되면 기존의 채팅방은 어떡하면 좋을지 상의하기
//    }

}

