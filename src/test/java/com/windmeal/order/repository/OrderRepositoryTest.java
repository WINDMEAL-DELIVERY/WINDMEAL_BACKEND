package com.windmeal.order.repository;

import static com.windmeal.Fixtures.aOrder;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderCreateRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest.OrderGroupRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest.OrderMenuRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest.OrderSpecRequestBuilder;
import com.windmeal.order.mapper.OrderRequestMapper;
import com.windmeal.order.repository.order.OrderRepository;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.Menu.MenuBuilder;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionGroup.OptionGroupBuilder;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.domain.OptionSpecification.OptionSpecificationBuilder;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.Store.StoreBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderRepositoryTest extends IntegrationTestSupport {

  @Autowired
  OrderRequestMapper orderRequestMapper;

  @Autowired
  OrderRepository orderRepository;
  @Autowired
  MemberRepository memberRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

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

  @DisplayName("")
  @Test
  void getOwnOrderedTotalPrice(){
    //given
    Member member = memberRepository.save(Member.builder().build());

    Order savedOrder1 = orderRepository.save(
        aOrder().orderer_id(member.getId()).deliveryFee(Money.wons(1000)).orderStatus(OrderStatus.DELIVERING).orderMenus(new ArrayList<>()).build());
    Order savedOrder2 = orderRepository.save(
        aOrder().orderer_id(member.getId()).deliveryFee(Money.wons(2000)).orderStatus(OrderStatus.ORDERED).orderMenus(new ArrayList<>()).build());
    Order savedOrder3 = orderRepository.save(
        aOrder().orderer_id(member.getId()).deliveryFee(Money.wons(3000)).orderStatus(OrderStatus.DELIVERED).orderMenus(new ArrayList<>()).build());
    Order savedOrder4 = orderRepository.save(
        aOrder().orderer_id(member.getId()).deliveryFee(Money.wons(1000)).orderStatus(OrderStatus.DELIVERED).orderMenus(new ArrayList<>()).build());
    Order savedOrder5 = orderRepository.save(
        aOrder().orderer_id(member.getId()).deliveryFee(Money.wons(1000)).orderStatus(OrderStatus.DELIVERED).orderMenus(new ArrayList<>()).build());
    Order savedOrder6 = orderRepository.save(
        aOrder().orderer_id(member.getId()).deliveryFee(Money.wons(5000)).orderStatus(OrderStatus.CANCELED).orderMenus(new ArrayList<>()).build());

    //when
    Integer totalPrice = orderRepository.getOwnOrderedTotalPrice(member.getId());

    //then
    Assertions.assertThat(totalPrice).isEqualTo(4000);
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