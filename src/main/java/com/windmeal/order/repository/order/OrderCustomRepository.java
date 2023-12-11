package com.windmeal.order.repository.order;

import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.order.dto.response.OrderMapListResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

  List<OrderMapListResponse> getOrderMapList(Long storeId, String eta,
      String storeCategory, Long placeId);

  RestSlice<OrderListResponse> getOrderList(Pageable pageable, Long storeId, String eta, String storeCategory,
      Long point, Long memberId);
}
