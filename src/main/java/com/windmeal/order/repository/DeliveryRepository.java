package com.windmeal.order.repository;

import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.DeliveryStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>{

  Optional<Delivery> findByDeliverIdAndOrderId(Long deliverId, Long orderId);
  Optional<Delivery> findByOrderId(Long orderId);

  List<Delivery> findAllByOrderId(Long id);

  Optional<Delivery> findByOrderIdAndDeliveryStatus(Long orderId, DeliveryStatus deliveryStatus);

  Optional<Delivery> findByOrderIdAndDeliveryStatusNot(Long orderId, DeliveryStatus deliveryStatus);
}
