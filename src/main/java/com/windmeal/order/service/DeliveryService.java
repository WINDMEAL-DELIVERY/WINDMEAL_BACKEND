package com.windmeal.order.service;


import com.windmeal.global.aop.annotation.DistributedLock;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.Order;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.exception.DeliverOrdererSameException;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.DeliveryRepository;
import com.windmeal.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

  /**
   * 고려사항 1. 동시성 이슈 해결
   */

  private final MemberRepository memberRepository;

  private final OrderRepository orderRepository;

  private final DeliveryRepository deliveryRepository;

  @Transactional
  public void createDelivery(DeliveryCreateRequest request) {
    Member deliver = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다."));

    if(order.getOrderer_id().equals(deliver.getId())){
      throw new DeliverOrdererSameException(ErrorCode.BAD_REQUEST,"본인의 주문을 배달할 수 없습니다.");
    }
    deliverySave(deliver, order);
  }

  @DistributedLock(key = "#order.getId()")
  public void deliverySave(Member deliver, Order order) {
    deliveryRepository.findByOrderId(order.getId())
        .ifPresent(
            delivery -> {
              throw new OrderAlreadyMatchedException(ErrorCode.BAD_REQUEST, "이미 매칭된 주문입니다.");
            });

    Delivery delivery = new Delivery(deliver, order);
    deliveryRepository.save(delivery);
    order.delivering();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deliverySaveWithoutDistributedLock(Member deliver, Order order) {
    deliveryRepository.findByOrderId(order.getId())
        .ifPresent(
            delivery -> {
              throw new OrderAlreadyMatchedException(ErrorCode.BAD_REQUEST, "이미 매칭된 주문입니다.");
            });

    Delivery delivery = new Delivery(deliver, order);
    deliveryRepository.save(delivery);
    order.delivering();
  }
}
