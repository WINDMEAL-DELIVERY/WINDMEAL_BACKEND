package com.windmeal.order.repository;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.order.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderCustomRepositoryImplTest extends IntegrationTestSupport {

  @Autowired
  OrderRepository orderRepository;

  @DisplayName("")
  @Test
  void getOrderList() {
    //given
    //when
//    orderRepository.getOrderMapList(null,null,null,null,null);
    //then
//    for (OrderListResponse orderListResponse : allOrder) {
//      System.out.println("orderListDetailResponse = " + orderListResponse);
//    }
  }
}