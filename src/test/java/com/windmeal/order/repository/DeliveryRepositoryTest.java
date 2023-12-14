package com.windmeal.order.repository;

import static com.windmeal.Fixtures.aOrder;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.PlaceRepository;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.domain.Order;
import com.windmeal.order.repository.delivery.DeliveryRepository;
import com.windmeal.order.repository.order.OrderRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeliveryRepositoryTest extends IntegrationTestSupport {

  @Autowired
  DeliveryRepository deliveryRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  PlaceRepository placeRepository;
  @AfterEach
  void tearDown() {
    deliveryRepository.deleteAllInBatch();;
    orderRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
    placeRepository.deleteAllInBatch();
  }

  @DisplayName("")
  @Test
  void findByOrderIdAndDeliveryStatus() {
    //given
    Member member = memberRepository.save(Member.builder().build());
    Order order = orderRepository.save(Order.builder().eta(LocalTime.MAX).orderMenus(new ArrayList<>()).build());
    deliveryRepository.save(
        Delivery.builder()
            .deliver(member)
            .order(order)
            .deliveryStatus(DeliveryStatus.DELIVERING)
            .build());
    //when
    Assertions.assertThat(deliveryRepository.findByOrderIdAndDeliveryStatus(order.getId(),DeliveryStatus.DELIVERING)
        .isPresent()).isTrue();
    Assertions.assertThat(deliveryRepository.findByOrderIdAndDeliveryStatus(order.getId(),DeliveryStatus.CANCELED)
        .isPresent()).isFalse();
    Assertions.assertThat(deliveryRepository.findByOrderIdAndDeliveryStatus(order.getId(),DeliveryStatus.DELIVERED)
        .isPresent()).isFalse();
    //then
  }

  @DisplayName("내가 배달한 총 금액 조회")
  @Test
  void getOwnDeliveredTotalPrice(){
      //given
    Member member = memberRepository.save(Member.builder().build());
    Order savedOrder1 = orderRepository.save(aOrder().deliveryFee(Money.wons(1000)).build());
    Order savedOrder2 = orderRepository.save(aOrder().deliveryFee(Money.wons(2000)).build());
    Order savedOrder3 = orderRepository.save(aOrder().deliveryFee(Money.wons(3000)).build());

    deliveryRepository.save(
        Delivery.builder()
            .deliver(member)
            .order(savedOrder1)
            .deliveryStatus(DeliveryStatus.DELIVERING)
            .build());
    deliveryRepository.save(
        Delivery.builder()
            .deliver(member)
            .order(savedOrder2)
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .build());
    deliveryRepository.save(
        Delivery.builder()
            .deliver(member)
            .order(savedOrder3)
            .deliveryStatus(DeliveryStatus.DELIVERED)
            .build());
    //when
    Integer totalPrice = deliveryRepository.getOwnDeliveredTotalPrice(member.getId());
    //then
    Assertions.assertThat(totalPrice).isEqualTo(5000);
  }
}