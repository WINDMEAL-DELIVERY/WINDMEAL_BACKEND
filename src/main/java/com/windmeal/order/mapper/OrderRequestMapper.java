package com.windmeal.order.mapper;

import static java.util.stream.Collectors.toList;

import com.windmeal.model.place.Place;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderMenu;
import com.windmeal.order.domain.OrderMenuOptionGroup;
import com.windmeal.order.domain.OrderMenuOptionSpecification;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestMapper {

  public Order mapFrom(OrderCreateRequest request) {
    return new Order(
        request.getMemberId(),
        request.getStoreId(),
        request.getDescription(),
        LocalDateTime.now(),
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
        .price(orderMenuRequest.getPrice())
        .name(orderMenuRequest.getName())
        .count(orderMenuRequest.getCount())
        .groups(orderMenuRequest.getGroups().stream()
            .map(this::toOrderMenuOptionGroup)
            .collect(toList()))
        .build();
  }

  private OrderMenuOptionGroup toOrderMenuOptionGroup(OrderGroupRequest orderGroupRequest) {
    return OrderMenuOptionGroup.builder()
        .option_group_id(orderGroupRequest.getOptionGroupId())
        .name(orderGroupRequest.getName())
        .specs(orderGroupRequest.getSpecs().stream()
            .map(this::toOrderMenuOptionSpecification).collect(toList()))
        .build();
  }

  private OrderMenuOptionSpecification toOrderMenuOptionSpecification(
      OrderSpecRequest orderSpecRequest) {
    return OrderMenuOptionSpecification.builder()
        .option_specification_id(orderSpecRequest.getOptionSpecId())
        .name(orderSpecRequest.getName())
        .price(orderSpecRequest.getPrice())
        .build();
  }

}
