package com.windmeal.order.repository;

import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.OrderMenuOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuOptionGroupRepository extends JpaRepository<OrderMenuOptionGroup, Long> {

}
