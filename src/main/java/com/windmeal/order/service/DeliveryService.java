package com.windmeal.order.service;


import com.windmeal.global.aop.annotation.DistributedLock;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.event.EventPublisher;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderCancel;
import com.windmeal.order.domain.event.DeliveryMatchEvent;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.dto.request.DeliveryCancelRequest;
import com.windmeal.order.dto.response.DeliveryListResponse;
import com.windmeal.order.dto.response.OrderingListResponse;
import com.windmeal.order.exception.DeliverOrdererSameException;
import com.windmeal.order.exception.DeliveryNotFoundException;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.delivery.DeliveryRepository;
import com.windmeal.order.repository.order.OrderCancelRepository;
import com.windmeal.order.repository.order.OrderRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

  private final OrderCancelRepository orderCancelRepository;
  @Transactional
  public void createDelivery(DeliveryCreateRequest request) {
    Member deliver = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다."));

    Member orderer = memberRepository.findById(order.getOrderer_id())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

    if(order.getOrderer_id().equals(deliver.getId())){
      throw new DeliverOrdererSameException(ErrorCode.BAD_REQUEST,"본인의 주문을 배달할 수 없습니다.");
    }
    deliverySave(deliver, order);

    //TODO orderer 의 토큰값으로 배달 성사 알람
    EventPublisher.publish(new DeliveryMatchEvent("test"));
  }

  @DistributedLock(key = "#order.getId()")
  public void deliverySave(Member deliver, Order order) {
    deliveryRepository.findByOrderIdAndDeliveryStatusNot(order.getId(),DeliveryStatus.CANCELED)
        .ifPresent(
            delivery -> {
              throw new OrderAlreadyMatchedException(ErrorCode.BAD_REQUEST, "이미 매칭된 주문입니다.");
            });

    Delivery delivery = new Delivery(deliver, order,DeliveryStatus.DELIVERING);
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

    Delivery delivery = new Delivery(deliver, order,DeliveryStatus.DELIVERING);
    deliveryRepository.save(delivery);
    order.delivering();
  }

  /**
   * 배달 취소 시 유의 점
   * "배달 중" 상태에서만 취소 가능.
   * @param request
   */
  @Transactional
  public void cancelDelivery(DeliveryCancelRequest request) {
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다."));
    Member cancelMember = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));
    Delivery delivery = deliveryRepository.findByOrderIdAndDeliveryStatus(order.getId(),
            DeliveryStatus.DELIVERING)
        .orElseThrow(() -> new DeliveryNotFoundException(ErrorCode.NOT_FOUND, "배달 요청이 존재하지 않습니다."));


    OrderCancel orderCancel = OrderCancel.builder()
        .cancelMember(cancelMember)
        .order(order)
        .delivery(delivery)
        .content(request.getContent()).build();
    orderCancelRepository.save(orderCancel);
  }


  public Slice<DeliveryListResponse> getOwnDelivering(Long memberId,Pageable pageable){
    return deliveryRepository.getOwnDelivering(memberId, LocalDate.now(), pageable);
  }

  public Slice<OrderingListResponse> getOwnOrdering(Long memberId,Pageable pageable){
    return deliveryRepository.getOwnOrdering(memberId, LocalDate.now(), pageable);
  }
}
