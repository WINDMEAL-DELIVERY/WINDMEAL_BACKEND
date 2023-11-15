package com.windmeal.order.repository;

import com.windmeal.order.domain.Order;
import com.windmeal.store.domain.Category;
import com.windmeal.store.repository.CategoryCustomRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
     {

}
