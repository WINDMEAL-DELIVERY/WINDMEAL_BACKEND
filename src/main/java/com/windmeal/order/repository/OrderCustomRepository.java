package com.windmeal.order.repository;

import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.order.dto.response.OrderListResponse;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

  RestSlice<OrderListResponse> getOrderList(Pageable pageable, Long storeId, String eta, String storeCategory,
      Long point, Long memberId);
}
