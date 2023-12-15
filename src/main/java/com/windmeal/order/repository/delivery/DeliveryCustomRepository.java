package com.windmeal.order.repository.delivery;

import com.windmeal.order.dto.response.DeliveryListResponse;
import com.windmeal.order.dto.response.OrderingListResponse;
import com.windmeal.order.dto.response.OwnDeliveryListResponse;
import com.windmeal.order.dto.response.OwnOrderListResponse;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.sql.ordering.antlr.OrderingSpecification.Ordering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DeliveryCustomRepository {
  Slice<DeliveryListResponse> getOwnDelivering(Long memberId, LocalDate today, Pageable pageable);
  Slice<OrderingListResponse> getOwnOrdering(Long memberId, LocalDate today, Pageable pageable);


  List<DeliveryListResponse> getOwnDeliveringWithoutPaging(Long memberId, LocalDate today, Pageable pageable);
  List<OrderingListResponse> getOwnOrderingWithoutPaging(Long memberId, LocalDate today, Pageable pageable);


  Slice<OwnDeliveryListResponse> getOwnDelivered(Long memberId, Pageable pageable, LocalDate startDate,
      LocalDate endDate, String storeCategory);
  Integer getOwnDeliveredTotalPrice(Long memberId);
}
