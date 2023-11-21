package com.windmeal.order.service;

import static com.windmeal.Fixtures.aMember;
import static com.windmeal.Fixtures.aOrder;
import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.dto.request.DeliveryCreateRequest.DeliveryCreateRequestBuilder;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.DeliveryRepository;
import com.windmeal.order.repository.OrderRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeliveryServiceTest extends IntegrationTestSupport {

  @Autowired
  DeliveryService deliveryService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  DeliveryRepository deliveryRepository;
  @Autowired
  OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    deliveryRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("배달 요청이 들어오면 성사된다.")
  @Test
  void createDelivery() {
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    DeliveryCreateRequest request = DeliveryCreateRequestBuilder()
        .orderId(order.getId())
        .memberId(deliver.getId()).build();
    //when
    deliveryService.createDelivery(request);

    //then
    Delivery delivery = deliveryRepository.findByDeliverIdAndOrderId(deliver.getId(), order.getId())
        .get();
    Order findOrder = orderRepository.findById(order.getId()).get();

    assertThat(delivery)
        .extracting(dlv -> dlv.getDeliver().getId(),dlv -> dlv.getOrder().getId())
        .containsExactly(deliver.getId(), order.getId());

    assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.DELIVERING);
  }


  @DisplayName("배달 요청이 들어왔을때 존재하지 않는 사용자의 요청이면 예외가 발생한다.")
  @Test
  void createDeliveryWithMemberNotFoundException() {
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    DeliveryCreateRequest request = DeliveryCreateRequestBuilder()
        .memberId(0L).build();
    //when


    //then
    assertThatThrownBy(() -> deliveryService.createDelivery(request))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("존재하지 않는 사용자입니다.");
  }

  @DisplayName("배달 요청이 들어왔을때 존재하지 않는 주문의 요청이면 예외가 발생한다.")
  @Test
  void createDeliveryWithOrderNotFoundException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    DeliveryCreateRequest request = DeliveryCreateRequestBuilder()
        .orderId(0L)
        .memberId(deliver.getId()).build();
    //when


    //then
    assertThatThrownBy(() -> deliveryService.createDelivery(request))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessage("존재하지 않는 주문입니다.");
  }


  private static DeliveryCreateRequestBuilder DeliveryCreateRequestBuilder() {
    return DeliveryCreateRequest.builder()
        .memberId(1L)
        .orderId(1L);
  }
}
