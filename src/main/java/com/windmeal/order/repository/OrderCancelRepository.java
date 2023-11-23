package com.windmeal.order.repository;

import com.windmeal.order.domain.OrderCancel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCancelRepository extends JpaRepository<OrderCancel, Long> {

  Optional<OrderCancel> findByOrderIdAndDeliveryId(Long orderId, Long deliveryId);
}
