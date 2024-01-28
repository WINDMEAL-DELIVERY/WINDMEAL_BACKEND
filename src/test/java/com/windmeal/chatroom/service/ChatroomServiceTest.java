package com.windmeal.chatroom.service;

import static com.windmeal.Fixtures.aStore;
import static org.assertj.core.api.Assertions.assertThat;

import com.windmeal.annotation.IntegrationTest;
import com.windmeal.chat.domain.ChatroomDocument;
import com.windmeal.chat.dto.request.ChatRoomSaveRequest;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.repository.ChatroomDocumentRepository;
import com.windmeal.chat.service.ChatRoomService;
import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.Place;
import com.windmeal.model.place.PlaceRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
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

  private Order createOrder(Long memberId, Long storeId) {
    return Order.builder()
        .orderStatus(OrderStatus.ORDERED)
        .orderMenus(new ArrayList<>())
        .orderer_id(memberId)
        .eta(LocalTime.now())
        .store_id(storeId)
        .build();
  }

  @Test
  @DisplayName("채팅방 생성 테스트 - 성공")
  public void createChatroomSuccess() {
    //given
    Member storeOwner = createMember("박사장", "장사왕", "사업학과");
    Member orderer = createMember("김주문", "주문왕", "주문학과");
    Member sender = createMember("최채팅", "채팅왕", "채팅학과");
    Member savedStoreOwner = memberRepository.save(storeOwner);
    Member savedOrderer = memberRepository.save(orderer);
    Member savedSender = memberRepository.save(sender);
    Store store = createStore(savedStoreOwner);
    Store savedStore = storeRepository.save(store);
    Order order = createOrder(savedOrderer.getId(), savedStore.getId());
    Order savedOrder = orderRepository.save(order);

    //when
    ChatRoomSaveRequest chatroomSaveRequest = createChatroomSaveRequest(savedOrder.getId());
    ChatRoomResponse chatRoom = chatRoomService.createChatRoom(chatroomSaveRequest,
        savedSender.getId());

    //then
    ChatroomDocument chatroomDocument = chatroomDocumentRepository.findById(chatRoom.getRoomId())
        .get();
    assertThat(chatroomDocument.getOrderId()).isEqualTo(savedOrder.getId());
    assertThat(chatroomDocument.getOwnerId()).isEqualTo(savedOrderer.getId());
    assertThat(chatroomDocument.getGuestId()).isEqualTo(savedSender.getId());

  }

}
