package com.windmeal.chatroom.service;

import static com.windmeal.Fixtures.aStore;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.windmeal.annotation.IntegrationTest;
import com.windmeal.chat.domain.ChatroomDocument;
import com.windmeal.chat.dto.request.ChatRoomSaveRequest;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.repository.ChatroomDocumentRepository;
import com.windmeal.chat.service.ChatRoomService;
import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.Place;
import com.windmeal.model.place.PlaceRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.repository.order.OrderRepository;
import com.windmeal.store.domain.Store;
import com.windmeal.store.repository.StoreRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class ChatroomServiceTest {

  @Autowired
  private ChatRoomService chatRoomService;
  @Autowired
  private ChatroomDocumentRepository chatroomDocumentRepository;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  StoreRepository storeRepository;
  @Autowired
  PlaceRepository placeRepository;


  @AfterEach
  void tearDown() {
    orderRepository.deleteAllInBatch();
    storeRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
    chatroomDocumentRepository.deleteAll();
  }


  private ChatRoomSaveRequest createChatroomSaveRequest(Long orderId) {
    return ChatRoomSaveRequest.of(orderId);
  }

  private Member createMember(String name, String nickname, String department) {
    return Member.builder()
        .department(department)
        .nickname(nickname)
        .name(name)
        .build();
  }

  private Place createPlace() {
    return Place.builder()
        .name("가천대학교")
        .latitude(37.5326)
        .longitude(127.0246)
        .build();
  }

  private Store createStore(Member owner) {
    return Store.builder()
        .name("신의 한컵")
        .owner(owner)
        .build();
  }

  private Order createOrder(Long memberId, Long storeId, OrderStatus orderStatus) {
    return Order.builder()
        .orderStatus(orderStatus)
        .orderMenus(new ArrayList<>())
        .orderer_id(memberId)
        .eta(LocalTime.now())
        .store_id(storeId)
        .build();
  }

  @Test
  @DisplayName("채팅방 생성 테스트 - 성공(최초 매칭)")
  public void createChatroomSuccess() {
    //given
    Member storeOwner = memberRepository.save(createMember("임사장", "장사왕", "사업학과"));
    Member orderer = memberRepository.save(createMember("김주문", "주문왕", "주문학과"));
    Member guest = memberRepository.save(createMember("최채팅", "채팅왕", "채팅학과"));
    Store store = storeRepository.save(createStore(storeOwner));
    Order order = orderRepository.save(
        createOrder(orderer.getId(), store.getId(), OrderStatus.ORDERED));

    //when
    ChatRoomSaveRequest chatroomSaveRequest = createChatroomSaveRequest(order.getId());
    ChatRoomResponse chatRoom = chatRoomService.createChatRoom(chatroomSaveRequest,
        guest.getId());

    //then
    ChatroomDocument chatroomDocument = chatroomDocumentRepository.findById(chatRoom.getRoomId())
        .get();
    assertThat(chatroomDocument.getOrderId()).isEqualTo(order.getId());
    assertThat(chatroomDocument.getOwnerId()).isEqualTo(orderer.getId());
    assertThat(chatroomDocument.getGuestId()).isEqualTo(guest.getId());
  }

  @Test
  @DisplayName("채팅방 생성 테스트 - 성공(취소된 주문 다시 매칭)")
  public void createCanceledOrderChatroomSuccess() {
    //given
    Member storeOwner = memberRepository.save(createMember("임사장", "장사왕", "사업학과"));
    Member orderer = memberRepository.save(createMember("김주문", "주문왕", "주문학과"));
    Member guest = memberRepository.save(createMember("최채팅", "채팅왕", "채팅학과"));
    Store store = storeRepository.save(createStore(storeOwner));
    Order order = orderRepository.save(
        createOrder(orderer.getId(), store.getId(), OrderStatus.CANCELED));

    //when
    ChatRoomSaveRequest chatroomSaveRequest = createChatroomSaveRequest(order.getId());
    ChatRoomResponse chatRoom = chatRoomService.createChatRoom(chatroomSaveRequest,
        guest.getId());

    //then
    ChatroomDocument chatroomDocument = chatroomDocumentRepository.findById(chatRoom.getRoomId())
        .get();
    assertThat(chatroomDocument.getOrderId()).isEqualTo(order.getId());
    assertThat(chatroomDocument.getOwnerId()).isEqualTo(orderer.getId());
    assertThat(chatroomDocument.getGuestId()).isEqualTo(guest.getId());
  }

  @Test
  @DisplayName("채팅방 생성 테스트 - 실패 : 주문 작성자를 찾을 수 없음")
  public void createChatroomOfInvalidOrderer() {
    //given
    Member storeOwner = memberRepository.save(createMember("임사장", "장사왕", "사업학과"));
    Member orderer = memberRepository.save(createMember("김주문", "주문왕", "주문학과"));
    Member guest = memberRepository.save(createMember("최채팅", "채팅왕", "채팅학과"));
    Store store = storeRepository.save(createStore(storeOwner));
    // 존재하지 않는 주문자의 아이디로 설정
    Order order = orderRepository.save(createOrder(32L, store.getId(), OrderStatus.ORDERED));

    //when
    ChatRoomSaveRequest chatroomSaveRequest = createChatroomSaveRequest(order.getId());

    //then
    assertThatThrownBy(() -> chatRoomService.createChatRoom(chatroomSaveRequest, guest.getId()))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("주문 작성자를 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("채팅방 생성 테스트 - 실패 : 주문 작성자를 찾을 수 없음")
  public void createChatroomOfInvalidGuest() {
    //given
    Member storeOwner = memberRepository.save(createMember("임사장", "장사왕", "사업학과"));
    Member orderer = memberRepository.save(createMember("김주문", "주문왕", "주문학과"));
    Member guest = memberRepository.save(createMember("최채팅", "채팅왕", "채팅학과"));
    Store store = storeRepository.save(createStore(storeOwner));
    Order order = orderRepository.save(
        createOrder(orderer.getId(), store.getId(), OrderStatus.ORDERED));

    //when
    ChatRoomSaveRequest chatroomSaveRequest = createChatroomSaveRequest(order.getId());

    //then
    // 존재하지 않는 요청자의 아이디로 설정
    assertThatThrownBy(() -> chatRoomService.createChatRoom(chatroomSaveRequest, 32L))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("해당 요청자를 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("채팅방 생성 테스트 - 실패 : 이미 매칭이 완료된 주문에 대해서 채팅방 생성")
  public void createChatroomOfAlreadyMatchedOrder() {
    //given
    Member storeOwner = memberRepository.save(createMember("임사장", "장사왕", "사업학과"));
    Member orderer = memberRepository.save(createMember("김주문", "주문왕", "주문학과"));
    Member guest = memberRepository.save(createMember("최채팅", "채팅왕", "채팅학과"));
    Store store = storeRepository.save(createStore(storeOwner));
    // 배달 중인 요청
    Order deliveringOrder = orderRepository.save(
        createOrder(orderer.getId(), store.getId(), OrderStatus.DELIVERING));
    // 배달 완료된 요청
    Order deliveredOrder = orderRepository.save(
        createOrder(orderer.getId(), store.getId(), OrderStatus.DELIVERED));

    //when
    ChatRoomSaveRequest delivering_request = createChatroomSaveRequest(deliveringOrder.getId());
    ChatRoomSaveRequest delivered_request = createChatroomSaveRequest(deliveredOrder.getId());

    //then
    // 존재하지 않는 요청자의 아이디로 설정
    assertThatThrownBy(() -> chatRoomService.createChatRoom(delivering_request, guest.getId()))
        .isInstanceOf(OrderAlreadyMatchedException.class)
        .hasMessage("이미 매칭된 주문입니다.");

    assertThatThrownBy(() -> chatRoomService.createChatRoom(delivered_request, guest.getId()))
        .isInstanceOf(OrderAlreadyMatchedException.class)
        .hasMessage("이미 매칭된 주문입니다.");
  }

}
