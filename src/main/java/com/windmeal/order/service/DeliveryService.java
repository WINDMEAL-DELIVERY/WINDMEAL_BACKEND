package com.windmeal.order.service;


import com.windmeal.global.aop.annotation.DistributedLock;
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
import com.windmeal.order.dto.response.OwnDeliveryListResponse;
import com.windmeal.order.exception.DeliverOrdererSameException;
import com.windmeal.order.exception.DeliveryNotFoundException;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.repository.delivery.DeliveryRepository;
import com.windmeal.order.repository.order.OrderCancelRepository;
import com.windmeal.order.repository.order.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {


  private final MemberRepository memberRepository;

  private final OrderRepository orderRepository;

  private final DeliveryRepository deliveryRepository;

  private final OrderCancelRepository orderCancelRepository;

  @Transactional
  public void createDelivery(DeliveryCreateRequest request) {
    Member deliver = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException());

    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException());

    Member orderer = memberRepository.findById(order.getOrderer_id())
        .orElseThrow(() -> new MemberNotFoundException());

    if (order.getOrderer_id().equals(deliver.getId())) {
      throw new DeliverOrdererSameException();
    }
    deliverySave(deliver, order);
    //TODO orderer 의 토큰값으로 배달 성사 알람
    EventPublisher.publish(new DeliveryMatchEvent(order.getSummary(), orderer.getToken()));
  }

  @DistributedLock(key = "#order.getId()")
  public void deliverySave(Member deliver, Order order) {
    deliveryRepository.findByOrderIdAndDeliveryStatusNot(order.getId(), DeliveryStatus.CANCELED)
        .ifPresent(
            delivery -> {
              throw new OrderAlreadyMatchedException();
            });

    Delivery delivery = new Delivery(deliver, order, DeliveryStatus.DELIVERING);
    deliveryRepository.save(delivery);
    order.delivering();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deliverySaveWithoutDistributedLock(Member deliver, Order order) {
    deliveryRepository.findByOrderId(order.getId())
        .ifPresent(
            delivery -> {
              throw new OrderAlreadyMatchedException();
            });

    Delivery delivery = new Delivery(deliver, order, DeliveryStatus.DELIVERING);
    deliveryRepository.save(delivery);
    order.delivering();
  }

  /**
   * 배달 취소 시 유의 점 "배달 중" 상태에서만 취소 가능.
   *
   * @param request
   */
  @Transactional
  public void cancelDelivery(DeliveryCancelRequest request) {
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException());
    Member cancelMember = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException());
    Delivery delivery = deliveryRepository.findByOrderIdAndDeliveryStatus(order.getId(),
            DeliveryStatus.DELIVERING)
        .orElseThrow(() -> new DeliveryNotFoundException());

    String token = getToken(order, cancelMember, delivery);

    OrderCancel orderCancel = OrderCancel.builder()
        .cancelMember(cancelMember)
        .order(order)
        .delivery(delivery)
        .content(request.getContent())
        .token(token).build();
    orderCancelRepository.save(orderCancel);
  }

  private String getToken(Order order, Member cancelMember, Delivery delivery) {

    if (cancelMember.equals(delivery.getDeliver())) {
      return cancelMember.getToken();
    } else {
      Member orderer = memberRepository.findById(order.getOrderer_id())
          .orElseThrow(() -> new MemberNotFoundException());

      return orderer.getToken();
    }
  }


  public List<DeliveryListResponse> getOwnDelivering(Long memberId, Pageable pageable) {
    return deliveryRepository.getOwnDelivering(memberId, LocalDate.now(), pageable);
  }

  public List<OrderingListResponse> getOwnOrdering(Long memberId, Pageable pageable) {
    return deliveryRepository.getOwnOrdering(memberId, LocalDate.now(), pageable);
  }


  public int getOwnDeliveredTotalPrice(Long memberId) {
    return deliveryRepository.getOwnDeliveredTotalPrice(memberId);
  }


  public Slice<OwnDeliveryListResponse> getOwnDelivered(Long memberId, Pageable pageable,
      LocalDate startDate, LocalDate endDate, String storeCategory) {
    return deliveryRepository.getOwnDelivered(memberId, pageable, startDate, endDate,
        storeCategory);
  }
}
