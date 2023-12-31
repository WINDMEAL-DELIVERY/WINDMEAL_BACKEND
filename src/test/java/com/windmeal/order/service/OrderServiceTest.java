package com.windmeal.order.service;

import static com.windmeal.Fixtures.aMember;
import static com.windmeal.Fixtures.aOrder;
import static com.windmeal.Fixtures.aStore;
import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.Place;
import com.windmeal.model.place.PlaceRepository;
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
import com.windmeal.order.dto.request.OrderDeleteRequest;
import com.windmeal.order.dto.response.OrderCreateResponse;
import com.windmeal.order.exception.OrderAlreadyMatchedException;
import com.windmeal.order.exception.OrderNotFoundException;
import com.windmeal.order.exception.OrdererMissMatchException;
import com.windmeal.order.repository.order.OrderMenuOptionGroupRepository;
import com.windmeal.order.repository.order.OrderMenuOptionSpecificationRepository;
import com.windmeal.order.repository.order.OrderMenuRepository;
import com.windmeal.order.repository.order.OrderRepository;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.Menu.MenuBuilder;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionGroup.OptionGroupBuilder;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.domain.OptionSpecification.OptionSpecificationBuilder;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.Store.StoreBuilder;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.StoreRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTestSupport {

  @Autowired
  private OrderService orderService;

  @Autowired
  OrderMenuRepository orderMenuRepository;

  @Autowired
  OrderMenuOptionSpecificationRepository optionSpecificationRepository;

  @Autowired
  OrderMenuOptionGroupRepository orderMenuOptionGroupRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  StoreRepository storeRepository;

  @Autowired
  MenuRepository menuRepository;


  @Autowired
  PlaceRepository placeRepository;
  @AfterEach
  void tearDown() {
    optionSpecificationRepository.deleteAllInBatch();
    orderMenuOptionGroupRepository.deleteAllInBatch();
    orderMenuRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    storeRepository.deleteAllInBatch();
    menuRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
    placeRepository.deleteAllInBatch();
  }

  @DisplayName("주문 생성 시 주문자의 정보가 존재하지 않는다면 예외가 발생한다.")
  @Test
  void createOrderWithMemberNotFound() {
    //given
    Member orderer = aMember().id(0L).build();
    Store store = storeRepository.save(aStore().build());
    OrderCreateRequest request = getOrderCreateRequest(orderer, store);

    //when

    //then
    assertThatThrownBy(() -> orderService.createOrder(request))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("존재하지 않는 사용자입니다.");

  }

  @DisplayName("주문 생성을 할 수 있다.")
  @Test
  void createOrder() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Store store = storeRepository.save(aStore().build());
    OrderCreateRequest request = getOrderCreateRequest(orderer, store);

    //when
    OrderCreateResponse orderResponse = orderService.createOrder(request);

    //then
    Order findOrder = orderRepository.findById(orderResponse.getId()).get();
    Place place = placeRepository.findByNameAndLongitudeAndLatitude(orderResponse.getPlaceName(),
        orderResponse.getLongitude(),
        orderResponse.getLatitude()).get();

    assertThat(findOrder)
        .extracting(order -> order.getId(), order -> place.getName()
            , order -> order.getSummary(), order -> order.getDeliveryFee().wons()
            , order -> order.getTotalPrice().wons(), order -> order.getOrderStatus())
        .containsExactly(orderResponse.getId(), orderResponse.getPlaceName()
            , orderResponse.getSummary(), orderResponse.getDeliveryFee().wons()
            , orderResponse.getTotalPrice().wons(), OrderStatus.ORDERED);

  }

  private OrderCreateRequest getOrderCreateRequest(Member orderer, Store store) {
    List<Menu> menus = menuRepository.saveAll(Arrays.asList(
        aMenu().name("test1").price(Money.wons(1000))
            .optionGroups(
                Arrays.asList(aOptionGroup()
                    .optionSpecifications(
                        Arrays.asList(
                            aOptionSpecification().price(Money.wons(1000)).name("test1").build(),
                            aOptionSpecification().price(Money.wons(1500)).name("test2").build()
                        )

                    )
                    .build())
            ).build(),
        aMenu().name("test2").price(Money.wons(2000)).
            optionGroups(
                Arrays.asList(aOptionGroup()
                    .optionSpecifications(
                        Arrays.asList(
                            aOptionSpecification().price(Money.wons(2000)).name("test2").build()))
                    .build())
            ).build()
    ));
    OrderCreateRequest request = buildOrderCreateRequest()
        .storeId(store.getId())
        .memberId(orderer.getId())
        .eta(LocalTime.MAX)
        .placeName("test")
        .latitude(1.234)
        .longitude(2.345)
        .menus(
            Arrays.asList(
                buildOrderMenuRequest()
                    .menuId(menus.get(0).getId())
                    .count(2)
                    .name("test1")
                    .price(Money.wons(1000))
                    .groups(
                        Arrays.asList(
                            buildOrderGroupRequest()
                                .optionGroupId(menus.get(0).getOptionGroups().get(0).getId())
                                .specs(Arrays.asList(
                                    buildOrderSpecRequest()
                                        .optionSpecId(
                                            menus.get(0).getOptionGroups().get(0)
                                                .getOptionSpecifications().get(0).getId())
                                        .name("test1")

                                        .price(Money.wons(1000)).build(),
                                    buildOrderSpecRequest()
                                        .optionSpecId(
                                            menus.get(0).getOptionGroups().get(0)
                                                .getOptionSpecifications().get(1).getId())
                                        .name("test2")
                                        .price(Money.wons(1500)).build()
                                )).build()))
                    .build())
        )
        .build();
    return request;
  }

  @DisplayName("메뉴의 수량을 계산할 수 있다/")
  @Test
  void calculateTotalSize() {
    //given
    OrderCreateRequest request = OrderCreateRequest.builder()
        .storeId(1L)
        .memberId(1L)
        .deliveryFee(Money.MIN)
        .menus(
            Arrays.asList(buildOrderMenuRequest().count(4).price(Money.wons(2000)).build(), //8000
                buildOrderMenuRequest().count(3).price(Money.wons(1000)).build())).build(); //6000
    //when
    int totalSize = orderService.calculateTotalSize(request);
    //then
    assertThat(totalSize).isEqualTo(7);
  }

  @DisplayName("총 금액, 메뉴 1개, 메뉴 갯수에 따라 summary 를 생성할 수 있다.")
  @Test
  void getSummary() {
    //given
    String menuName = "test";
    Menu menu = Menu.builder().name(menuName).build();

    //when
    int totalPrice = 20000;
    String summaryWith10 = orderService.getSummary(Money.wons(totalPrice), "test",
        10);
    String summaryWith1 = orderService.getSummary(Money.wons(totalPrice), "test",
        1);
    //then
    assertThat(summaryWith1)
        .isEqualTo("test 20000원");

    assertThat(summaryWith10)
        .isEqualTo("test 외 9개 20000원");
  }

  @DisplayName("OrderCreateRequest에 대한 총 가격을 구할 수 있다.")
  @Test
  void calculateTotalPrice() {
    //given
    OrderCreateRequest request = OrderCreateRequest.builder()
        .storeId(1L)
        .memberId(1L)
        .deliveryFee(Money.MIN)
        .menus(
            Arrays.asList(buildOrderMenuRequest().price(Money.wons(2000)).build(), //8000
                buildOrderMenuRequest().price(Money.wons(1000)).build())).build(); //6000
    //when
    Money sum = Money.sum(request.getMenus(), OrderMenuRequest::calculatePrice);
    //then
    assertThat(sum.wons()).isEqualTo((14000));

  }

  @DisplayName("주문 요청을 삭제할 수 있다.")
  @Test
  void deleteOrder() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.ORDERED).orderer_id(orderer.getId())
            .orderMenus(new ArrayList<>()).build());

    OrderDeleteRequest request = OrderDeleteRequest.builder()
        .orderId(order.getId())
        .memberId(orderer.getId()).build();
    //when
    orderService.deleteOrder(request);
    //then
    assertThat(orderRepository.findById(request.getOrderId()).isPresent()).isFalse();
  }


  @DisplayName("주문 요청 삭제 시 존재하지 않는 주문이면 예외가 발생한다.")
  @Test
  void deleteOrderWith_OrderNotFoundException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.ORDERED).orderer_id(orderer.getId())
            .orderMenus(new ArrayList<>()).build());

    OrderDeleteRequest request = OrderDeleteRequest.builder()
        .orderId(0L)
        .memberId(orderer.getId()).build();
    //when

    //then
    assertThatThrownBy(() -> orderService.deleteOrder(request))
        .isInstanceOf(OrderNotFoundException.class)
        .hasMessage("존재하지 않는 주문입니다.");
  }

  @DisplayName("주문 삭제 요청이 주문중 상태가 아니라면 예외가 발생한다.")
  @Test
  void deleteOrderWith_OrderAlreadyMatchedException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.DELIVERING).orderer_id(orderer.getId())
            .orderMenus(new ArrayList<>()).build());

    OrderDeleteRequest request = OrderDeleteRequest.builder()
        .orderId(order.getId())
        .memberId(orderer.getId()).build();
    //when
    //then
    assertThatThrownBy(() -> orderService.deleteOrder(request))
        .isInstanceOf(OrderAlreadyMatchedException.class)
        .hasMessage("이미 배달 요청이 성사된 주문은 삭제할 수 없습니다.");
  }

  @DisplayName("주문 삭제 요청이 자신의 주문이 아니라면 예외가 발생한다.")
  @Test
  void deleteOrderWith_OrdererMissMatchException() {
    //given
    Member orderer = memberRepository.save(aMember().build());
    Order order = orderRepository.save(
        aOrder().orderStatus(OrderStatus.ORDERED).orderer_id(orderer.getId())
            .orderMenus(new ArrayList<>()).build());

    OrderDeleteRequest request = OrderDeleteRequest.builder()
        .orderId(order.getId())
        .memberId(0L).build();
    //when
    //then
    assertThatThrownBy(() -> orderService.deleteOrder(request))
        .isInstanceOf(OrdererMissMatchException.class)
        .hasMessage("본인의 주문만 삭제할 수 있습니다.");
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
        .price(Money.wons(2000))
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
        .price(Money.wons(1000))
        .optionSpecId(1L);

  }
}