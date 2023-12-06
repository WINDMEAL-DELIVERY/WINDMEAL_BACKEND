package com.windmeal.order.repository;

import com.windmeal.order.dto.response.DeliveryListResponse;
import com.windmeal.order.dto.response.OrderingListResponse;
import java.time.LocalDate;
import org.hibernate.sql.ordering.antlr.OrderingSpecification.Ordering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DeliveryCustomRepository {
  Slice<DeliveryListResponse> getOwnDelivering(Long memberId, LocalDate today, Pageable pageable);
  Slice<OrderingListResponse> getOwnOrdering(Long memberId, LocalDate today, Pageable pageable);
}
