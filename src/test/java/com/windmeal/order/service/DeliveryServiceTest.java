package com.windmeal.order.service;

import static com.windmeal.Fixtures.aDelivery;
import static com.windmeal.Fixtures.aMember;
import static com.windmeal.Fixtures.aOrder;
import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderCancel;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.dto.request.DeliveryCreateRequest.DeliveryCreateRequestBuilder;
import com.windmeal.order.dto.request.DeliveryCancelRequest;
import com.windmeal.order.exception.DeliverOrdererSameException;
import com.windmeal.order.exception.DeliveryNotFoundException;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.DeliveryRepository;
import com.windmeal.order.repository.OrderCancelRepository;
import com.windmeal.order.repository.OrderMenuOptionGroupRepository;
import com.windmeal.order.repository.OrderMenuOptionSpecificationRepository;
import com.windmeal.order.repository.OrderMenuRepository;
import com.windmeal.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

  @Autowired
  OrderMenuRepository orderMenuRepository;

  @Autowired
  OrderMenuOptionSpecificationRepository optionSpecificationRepository;

  @Autowired
  OrderMenuOptionGroupRepository orderMenuOptionGroupRepository;

  @Autowired
  OrderCancelRepository orderCancelRepository;

  @AfterEach
  void tearDown() {
    orderCancelRepository.deleteAllInBatch();
    deliveryRepository.deleteAllInBatch();
    optionSpecificationRepository.deleteAllInBatch();
    orderMenuOptionGroupRepository.deleteAllInBatch();
    orderMenuRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("배달 요청이 들어오면 성사된다.")
  @Test
  void createDelivery() {
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
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
        .extracting(dlv -> dlv.getDeliver().getId(), dlv -> dlv.getOrder().getId())
        .containsExactly(deliver.getId(), order.getId());

    assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.DELIVERING);
  }


  @DisplayName("배달 요청이 이미 매칭된 경우 예외가 발생한다.")
  @Test
  void deliverySaveWithAlreadyMatchException(){
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());


    //when
    deliveryService.deliverySave(deliver,order);

    //then


    assertThatThrownBy(() -> deliveryService.deliverySave(deliver,order))
        .isInstanceOf(OrderAlreadyMatchedException.class)
        .hasMessage("이미 매칭된 주문입니다.");
  }

  @DisplayName("배달 요청이 자신이 요청한 주문의 경우 예외가 발생한다.")
  @Test
  void deliverySaveWith_DeliverOrdererSameException(){
    //given

    Member orderer = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());


    //when
    DeliveryCreateRequest request = DeliveryCreateRequestBuilder()
        .orderId(order.getId())
        .memberId(orderer.getId()).build();
    //then
    assertThatThrownBy(() -> deliveryService.createDelivery(request))
        .isInstanceOf(DeliverOrdererSameException.class)
        .hasMessage("본인의 주문을 배달할 수 없습니다.");
  }

  @DisplayName("동시에 배달 요청이 있어도 1개의 배달만 성사된다.")
  @Test
  void deliverySaveMultiThreadWithAlreadyMatchException()throws InterruptedException {
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());


    //when
    int numberOfThreads = 20;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          // 분산락 적용 메서드 호출
          deliveryService.deliverySave(deliver,order);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    //then
    assertThat(deliveryRepository.findAllByOrderId(order.getId()).size())
        .isEqualTo(1);
  }

  @DisplayName("동시에 배달 요청이 오는 경우 분산락을 사용하지 않는 경우 문제가 발생한다.")
  @Test
  void deliverySaveWithoutDistributedLock_MultiThread_WithAlreadyMatchException()throws InterruptedException {
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());


    //when
    int numberOfThreads = 20;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          // 분산락 적용 메서드 호출
          deliveryService.deliverySaveWithoutDistributedLock(deliver,order);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    //then
    assertThat(deliveryRepository.findAllByOrderId(order.getId()).size())
        .isNotEqualTo(1);
  }


  @DisplayName("배달 요청이 들어왔을때 존재하지 않는 사용자의 요청이면 예외가 발생한다.")
  @Test
  void createDeliveryWithMemberNotFoundException() {
    //given

    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
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
    Order order = orderRepository.save(
        aOrder().orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    DeliveryCreateRequest request = DeliveryCreateRequestBuilder()
        .orderId(0L)
        .memberId(deliver.getId()).build();
    //when

    //then
    assertThatThrownBy(() -> deliveryService.createDelivery(request))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessage("존재하지 않는 주문입니다.");
  }

  @DisplayName("주문 취소 요청시 주문이 취소된다.")
  @Test
  void cancelOrder() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.DELIVERING).orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    Delivery delivery = deliveryRepository.save(aDelivery().deliveryStatus(DeliveryStatus.DELIVERING).order(order).deliver(deliver).build());

    Long cancelMemberId = orderer.getId();
    DeliveryCancelRequest cancelRequest = DeliveryCancelRequest.builder()
        .content("test")
        .memberId(cancelMemberId)
        .orderId(order.getId()).build();
    //when
    deliveryService.cancelDelivery(cancelRequest);

    //then
    Order findOrder = orderRepository.findById(order.getId()).orElse(null);
    Delivery findDelivery = deliveryRepository.findByOrderId(order.getId()).orElse(null);
    OrderCancel findOrderCancel = orderCancelRepository.findByOrderIdAndDeliveryId(order.getId(),
        findDelivery.getId()).orElse(null);

    assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    assertThat(findDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.CANCELED);
    assertThat(findOrderCancel)
        .extracting(orderCancel -> orderCancel.getOrder().getId(), orderCancel ->orderCancel.getCancelMember().getId(),
            orderCancel ->orderCancel.getDelivery().getId(), orderCancel ->orderCancel.getContent())
        .containsExactly(order.getId(),cancelMemberId
        ,findDelivery.getId(),cancelRequest.getContent());
  }



  @DisplayName("주문 취소 요청시 주문이 존재하지 않는 경우 예외가 발생한다.")
  @Test
  void cancelOrderWith_OrderNotFoundException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.DELIVERING).orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    Delivery delivery = deliveryRepository.save(aDelivery().order(order).deliver(deliver).build());

    Long cancelMemberId = orderer.getId();
    DeliveryCancelRequest cancelRequest = DeliveryCancelRequest.builder()
        .content("test")
        .memberId(cancelMemberId)
        .orderId(0L).build();
    //when


    //then
    assertThatThrownBy(() -> deliveryService.cancelDelivery(cancelRequest))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessage("존재하지 않는 주문입니다.");
  }

  @DisplayName("주문 취소 요청시 요청자가 존재하지 않는 경우 예외가 발생한다.")
  @Test
  void cancelOrderWith_MemberNotFoundException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.DELIVERING).orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    Delivery delivery = deliveryRepository.save(aDelivery().order(order).deliver(deliver).build());

    Long cancelMemberId = orderer.getId();
    DeliveryCancelRequest cancelRequest = DeliveryCancelRequest.builder()
        .content("test")
        .memberId(0L)
        .orderId(order.getId()).build();
    //when


    //then
    assertThatThrownBy(() -> deliveryService.cancelDelivery(cancelRequest))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("존재하지 않는 사용자입니다.");
  }

  @DisplayName("주문 취소 요청시 배달 요청 내역이 존재하지 않는 경우 예외가 발생한다.")
  @Test
  void cancelOrderWith_DeliveryNotFoundException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Member deliver = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.DELIVERING).orderer_id(orderer.getId()).orderMenus(new ArrayList<>()).build());
    Delivery delivery = deliveryRepository.save(aDelivery().order(order).deliver(deliver).build());
    delivery.canceled();

    Long cancelMemberId = orderer.getId();
    DeliveryCancelRequest cancelRequest = DeliveryCancelRequest.builder()
        .content("test")
        .memberId(cancelMemberId)
        .orderId(order.getId()).build();
    //when


    //then
    assertThatThrownBy(() -> deliveryService.cancelDelivery(cancelRequest))
        .isInstanceOf(DeliveryNotFoundException.class)
        .hasMessage("배달 요청이 존재하지 않습니다.");
  }


  private static DeliveryCreateRequestBuilder DeliveryCreateRequestBuilder() {
    return DeliveryCreateRequest.builder()
        .memberId(1L)
        .orderId(1L);
  }
}
