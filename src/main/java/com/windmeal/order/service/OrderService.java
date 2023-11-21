package com.windmeal.order.service;

import com.windmeal.generic.domain.Money;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.response.OrderCreateResponse;
import com.windmeal.order.mapper.OrderRequestMapper;
import com.windmeal.order.repository.OrderRepository;
import com.windmeal.order.validator.OrderValidator;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
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
    String summary = getSummary(totalPrice, getMenu(request), request.getMenus().size());
    order.place(totalPrice,summary);
    Order savedOrder = orderRepository.save(order);
    return OrderCreateResponse.toResponse(savedOrder);
  }

  private Menu getMenu(OrderCreateRequest request) {
    return menuRepository.findById(request.getMenus().get(0).getMenuId())
        .orElseThrow(() -> new MenuNotFoundException(ErrorCode.NOT_FOUND, "메뉴가 존재하지 않습니다."));
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
