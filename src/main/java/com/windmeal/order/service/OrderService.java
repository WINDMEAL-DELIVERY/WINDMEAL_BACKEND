package com.windmeal.order.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.dto.OrderCreateRequest;
import com.windmeal.order.mapper.OrderRequestMapper;
import com.windmeal.order.repository.OrderRepository;
import com.windmeal.order.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderValidator orderValidator;
  private final MemberRepository memberRepository;
  private final OrderRepository orderRepository;
  private final OrderRequestMapper orderRequestMapper;

  @Transactional
  public void createOrder(OrderCreateRequest request) {
    memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

    orderValidator.validate(request);
    Order order = orderRequestMapper.mapFrom(request);
    orderRepository.save(order);

  }
}
