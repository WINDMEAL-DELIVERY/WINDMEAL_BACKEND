package com.windmeal.order.repository;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.order.dto.response.OrderListResponse;
import java.util.List;
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
//    List<OrderListResponse> allOrder = orderRepository.getOrderList(storeId, eta, storeCategory);
    //then
//    for (OrderListResponse orderListResponse : allOrder) {
//      System.out.println("orderListDetailResponse = " + orderListResponse);
//    }
  }
}