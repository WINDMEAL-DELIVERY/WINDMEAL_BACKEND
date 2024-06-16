package com.windmeal.chat.service;

import com.windmeal.chat.domain.ChatroomDocument;
import com.windmeal.chat.dto.request.ChatRoomSaveRequest;
import com.windmeal.chat.dto.response.ChatRoomExistsResponse;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.repository.ChatroomDocumentRepository;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
   * 채팅방 생성을 관장하는 메서드 <br/> 생성 시에는 고려해줄 것들이 많기 때문에, 불가피하게 api 서버에서 이를 담당하게 되었다. 하지만 생성 이외의 기능들은 모두
   * 채팅 서버에서 담당한다.
   *
   * @param requestDTO
   * @return {@link com.windmeal.chat.dto.response.ChatRoomResponse}
   */
  @Transactional
  public ChatRoomResponse createChatRoom(ChatRoomSaveRequest requestDTO, Long currentMemberId) {

    Order order = orderRepository.findById(requestDTO.getOrderId())
        .orElseThrow(OrderNotFoundException::new);
    Member owner = memberRepository.findById(order.getOrderer_id())
        .orElseThrow(() -> new MemberNotFoundException("주문 작성자를 찾을 수 없습니다."));
    Member guest = memberRepository.findById(currentMemberId)
        .orElseThrow(() -> new MemberNotFoundException("해당 요청자를 찾을 수 없습니다."));

    // 주문이 진행중이면 채팅방을 생성할 수 없다.
    if (!order.getOrderStatus().equals(OrderStatus.ORDERED) && !order.getOrderStatus()
        .equals(OrderStatus.CANCELED)) {
      throw new OrderAlreadyMatchedException();
    }
    ChatroomDocument chatRoom = chatroomDocumentRepository.save(
        requestDTO.toDocument(owner, guest, order));
    return ChatRoomResponse.of(chatRoom.getId(), chatRoom.getOwnerId(), chatRoom.getGuestId(),
        chatRoom.getOrderId(), owner.getToken());
  }


  public ChatRoomExistsResponse checkChatroom (Long orderId, Long currentMemberId) {
    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    ChatroomDocument chatroomDocument =
        chatroomDocumentRepository.findByOrderIdAndGuestId(order.getId(), currentMemberId);
    return new ChatRoomExistsResponse(chatroomDocument != null ? chatroomDocument.getId() : null);
  }

}

