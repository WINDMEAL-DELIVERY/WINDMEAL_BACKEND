package com.windmeal.order.repository.order;

import com.windmeal.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderCustomRepository
{

}
