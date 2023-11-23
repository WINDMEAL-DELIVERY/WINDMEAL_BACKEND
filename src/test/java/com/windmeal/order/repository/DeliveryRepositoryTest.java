package com.windmeal.order.repository;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.domain.Order;
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

  @AfterEach
  void tearDown() {
    deliveryRepository.deleteAllInBatch();;
    orderRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("")
  @Test
  void findByOrderIdAndDeliveryStatus() {
    //given
    Member member = memberRepository.save(Member.builder().build());
    Order order = orderRepository.save(Order.builder().orderMenus(new ArrayList<>()).build());
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
}