package com.windmeal.order.service;

import com.windmeal.generic.domain.Money;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderDeleteRequest;
import com.windmeal.order.dto.response.OrderCreateResponse;
import com.windmeal.order.dto.response.OrderDetailResponse;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.exception.OrdererMissMatchException;
import com.windmeal.order.mapper.OrderRequestMapper;
import com.windmeal.order.repository.OrderRepository;
import com.windmeal.order.validator.OrderValidator;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderValidator orderValidator;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private final OrderRepository orderRepository;
  private final OrderRequestMapper orderRequestMapper;

  @Transactional
  public OrderCreateResponse createOrder(OrderCreateRequest request) {
    memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

    orderValidator.validate(request);
    Order order = orderRequestMapper.mapFrom(request);
    Money totalPrice = calculateTotalPrice(request);
    String summary = getSummary(totalPrice, getMenu(request), calculateTotalSize(request));
    order.place(totalPrice,summary);
    Order savedOrder = orderRepository.save(order);
    return OrderCreateResponse.toResponse(savedOrder);
  }


  // 배달이 성사되지 않은 ORDERED 상태의 주문 요청만 삭제 가능.
  @Transactional
  public void deleteOrder(OrderDeleteRequest request) {
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다."));

    if(!order.getOrderStatus().equals(OrderStatus.ORDERED)){
      throw new OrderAlreadyMatchedException(ErrorCode.BAD_REQUEST, "이미 배달 요청이 성사된 주문은 삭제할 수 없습니다.");
    }

    if(!order.getOrderer_id().equals(request.getMemberId())){
      throw new OrdererMissMatchException(ErrorCode.BAD_REQUEST, "본인의 주문만 삭제할 수 있습니다.");
    }

    orderRepository.deleteById(request.getOrderId());
  }

  public Page<OrderListResponse> getAllOrder(Pageable pageable, Long storeId, String eta, String storeCategory,
      Point point) {
    return orderRepository.getOrderList(pageable,storeId,eta,storeCategory,point);
  }

  public OrderDetailResponse getOrderDetail(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다."));
    return null;
  }

  private Menu getMenu(OrderCreateRequest request) {
    return menuRepository.findById(request.getMenus().get(0).getMenuId())
        .orElseThrow(() -> new MenuNotFoundException(ErrorCode.NOT_FOUND, "메뉴가 존재하지 않습니다."));
  }
  public int calculateTotalSize(OrderCreateRequest request) {
    return request.getMenus().stream().mapToInt(menus->menus.getCount()).sum();
  }

  public String getSummary(Money totalPrice, Menu menu, int menuCount) {
    return menuCount == 1 ?
        String.format("%s %s원", menu.getName(), totalPrice.wons()) :
        String.format("%s 외 %s개 %s원", menu.getName(), menuCount - 1, totalPrice.wons());
  }

  public Money calculateTotalPrice(OrderCreateRequest request) {
      return Money.sum(request.getMenus(), OrderMenuRequest::calculatePrice);
  }



  private static Long getMenuCount(OrderCreateRequest request) {
    return request.getMenus().stream().count();
  }





}
