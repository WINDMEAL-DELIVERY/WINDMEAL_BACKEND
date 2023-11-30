package com.windmeal.order.repository;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.order.domain.Order;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderCreateRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest.OrderGroupRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest.OrderMenuRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest.OrderSpecRequestBuilder;
import com.windmeal.order.mapper.OrderRequestMapper;
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

class OrderRepositoryTest extends IntegrationTestSupport {

  @Autowired
  OrderRequestMapper orderRequestMapper;

  @Autowired
  OrderRepository orderRepository;

//  @DisplayName("")
//  @Test
//  void save(){
//      //given
//    OrderCreateRequest build = buildOrderCreateRequest().build();
//    Order order = orderRequestMapper.mapFrom(build);
//    //when
//    orderRepository.save(order);
//      //then
//  }


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