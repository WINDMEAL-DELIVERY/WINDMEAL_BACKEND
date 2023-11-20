package com.windmeal.order.mapper;

import static java.util.stream.Collectors.toList;

import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderMenu;
import com.windmeal.order.domain.OrderMenuOptionGroup;
import com.windmeal.order.domain.OrderMenuOptionSpecification;
import com.windmeal.order.dto.OrderCreateRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderSpecRequest;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestMapper {

  public Order mapFrom(OrderCreateRequest request) {
    return new Order(
        request.getMemberId(),
        request.getStoreId(),
        LocalDateTime.now(),
        request.getDestination(),
        request.getEta(),
        request.getDeliveryFee(),
        request.getMenus().stream()
            .map(this::toOrderMenu)
            .collect(toList())
    );

  }

  private OrderMenu toOrderMenu(OrderMenuRequest orderMenuRequest) {
    return OrderMenu.builder()
        .menu_id(orderMenuRequest.getMenuId())
        .count(orderMenuRequest.getCount())
        .groups(orderMenuRequest.getGroups().stream()
            .map(this::toOrderMenuOptionGroup)
            .collect(toList()))
        .build();
  }

  private OrderMenuOptionGroup toOrderMenuOptionGroup(OrderGroupRequest orderGroupRequest) {
    return OrderMenuOptionGroup.builder()
        .option_group_id(orderGroupRequest.getOptionGroupId())
        .specs(orderGroupRequest.getSpecs().stream()
            .map(this::toOrderMenuOptionSpecification).collect(toList()))
        .build();
  }

  private OrderMenuOptionSpecification toOrderMenuOptionSpecification(
      OrderSpecRequest orderSpecRequest) {
    return OrderMenuOptionSpecification.builder()
        .option_specification_id(orderSpecRequest.getOptionSpecId())
        .build();
  }

}
