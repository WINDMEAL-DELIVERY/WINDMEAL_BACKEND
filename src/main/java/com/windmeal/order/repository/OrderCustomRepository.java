package com.windmeal.order.repository;

import com.windmeal.order.dto.response.OrderListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

public interface OrderCustomRepository {

  Page<OrderListResponse> getOrderList(Pageable pageable, Long storeId, String eta, String storeCategory,
      Point point);
}