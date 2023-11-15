package com.windmeal.order.controller;

import com.windmeal.order.dto.OrderCreateRequest;
import com.windmeal.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/order")
  public void createOrder(@RequestBody OrderCreateRequest request){
    orderService.createOrder(request);
  }

}
