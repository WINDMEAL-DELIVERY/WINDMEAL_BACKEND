package com.windmeal.order.repository;

import com.windmeal.order.domain.Delivery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>{

  Optional<Delivery> findByDeliverIdAndOrderId(Long deliverId, Long orderId);
}
