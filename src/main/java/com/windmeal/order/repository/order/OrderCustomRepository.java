package com.windmeal.order.repository.order;

import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.order.dto.response.OwnOrderListResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface OrderCustomRepository {

  RestSlice<OrderListResponse> getOrderList(Pageable pageable, Long storeId, String eta, String storeCategory,
      Long point, Long memberId);
  Integer getOwnOrderedTotalPrice(Long memberId);

  Slice<OwnOrderListResponse> getOwnOrdered(Long memberId, Pageable pageable,  LocalDate startDate,LocalDate endDate, String category);
}
