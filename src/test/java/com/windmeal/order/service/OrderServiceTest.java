package com.windmeal.order.service;

import static org.junit.jupiter.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.order.dto.OrderCreateRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderCreateRequestBuilder;
import com.windmeal.order.dto.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderGroupRequest.OrderGroupRequestBuilder;
import com.windmeal.order.dto.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderMenuRequest.OrderMenuRequestBuilder;
import com.windmeal.order.dto.OrderCreateRequest.OrderSpecRequest;
import com.windmeal.order.dto.OrderCreateRequest.OrderSpecRequest.OrderSpecRequestBuilder;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.Menu.MenuBuilder;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionGroup.OptionGroupBuilder;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.domain.OptionSpecification.OptionSpecificationBuilder;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.Store.StoreBuilder;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTestSupport {

  @Autowired
  private OrderService orderService;

  @DisplayName("주문 생성을 할 수 있다.")
  @Test
  void createOrder() {
    //given
    OrderCreateRequest request = buildOrderCreateRequest().build();

    //when
    orderService.createOrder(request);

    //then
  }

  private static OrderCreateRequestBuilder buildOrderCreateRequest() {
    return OrderCreateRequest.builder()
        .storeId(1L)
        .memberId(1L)
        .deliveryFee(Money.MIN)
        .menus(Arrays.asList(buildOrderMenuRequest().build()));
  }

  public static StoreBuilder aShop() {
    return Store.builder()
        .name("오겹돼지");
  }

  public static MenuBuilder aMenu() {
    return Menu.builder()
        .optionGroups(Arrays.asList(aOptionGroup().build()))
        ;
  }

  public static OptionGroupBuilder aOptionGroup() {
    return OptionGroup.builder()
        .id(1L)
        .isMultipleOption(true)
        .isEssentialOption(true)
        .optionSpecifications(Arrays.asList(aOptionSpecification().build(),
            aOptionSpecification().build()))
        ;
  }

  public static OptionSpecificationBuilder aOptionSpecification() {
    return OptionSpecification.builder();
  }

  public static OrderMenuRequestBuilder buildOrderMenuRequest() {
    return OrderMenuRequest
        .builder()
        .menuId(1L)
        .count(2)
        .groups(Arrays.asList(
            buildOrderGroupRequest()
                .optionGroupId(1L)
                .specs(Arrays.asList(
                    buildOrderSpecRequest().optionSpecId(1L).build(),
                    buildOrderSpecRequest().optionSpecId(2L).build()
                )).build()
        ));
  }

  public static OrderGroupRequestBuilder buildOrderGroupRequest() {
    return OrderGroupRequest
        .builder()
        .optionGroupId(1L)
        ;

  }

  public static OrderSpecRequestBuilder buildOrderSpecRequest() {
    return OrderSpecRequest
        .builder()
        .optionSpecId(1L);

  }
}