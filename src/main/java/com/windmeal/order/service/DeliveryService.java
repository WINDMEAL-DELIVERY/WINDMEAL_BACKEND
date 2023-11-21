package com.windmeal.order.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.Order;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.DeliveryRepository;
import com.windmeal.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

  /**
   * 고려사항
   * 1. 동시성 이슈 해결
   * 2. event 방식 사용 할건지 생각해볼 것
   */

  private final MemberRepository memberRepository;

  private final OrderRepository orderRepository;

  private final DeliveryRepository deliveryRepository;

  @Transactional
  public void createDelivery(DeliveryCreateRequest request){
    Member deliver = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다."));

    Delivery delivery = new Delivery(deliver, order);
    deliveryRepository.save(delivery);
    order.delivering();
  }
}
