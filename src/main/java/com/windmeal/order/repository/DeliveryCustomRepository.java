package com.windmeal.order.repository;

import com.windmeal.order.dto.response.DeliveryListResponse;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryCustomRepository {
  Page<DeliveryListResponse> getOwnDelivering(Long memberId, LocalDate today, Pageable pageable);
  Page<DeliveryListResponse> getOwnOrdering(Long memberId, LocalDate today, Pageable pageable);
}
