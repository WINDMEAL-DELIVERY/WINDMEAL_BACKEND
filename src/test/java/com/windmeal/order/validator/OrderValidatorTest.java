package com.windmeal.order.validator;

import static org.mockito.Mockito.mock;

import com.windmeal.generic.domain.Money;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderCreateRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest.OrderGroupRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest.OrderMenuRequestBuilder;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest.OrderSpecRequestBuilder;
import com.windmeal.order.exception.OrderDeliveryFeeException;
import com.windmeal.order.exception.OrderEmptyException;
import com.windmeal.order.exception.OrderGroupEmptyException;
import com.windmeal.order.exception.OrderGroupIsNotMultipleException;
import com.windmeal.order.exception.OrderMenuCountException;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.Menu.MenuBuilder;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionGroup.OptionGroupBuilder;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.domain.OptionSpecification.OptionSpecificationBuilder;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.Store.StoreBuilder;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.exception.StoreNotOpenException;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.StoreRepository;
import com.windmeal.store.validator.StoreValidator;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class OrderValidatorTest {

  @InjectMocks
  private OrderValidator validator;

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private StoreValidator storeValidator;

  @Mock
  private MenuRepository menuRepository;

  @DisplayName("가게 운영 시간이 아닌 경우 실패한다.")
  @Test
  void validateStoreIsOpen() {
    //given
    Store store = aShop()
        .openTime(LocalTime.now().minus(5, ChronoUnit.HOURS))
        .closeTime(LocalTime.now().minus(50, ChronoUnit.MINUTES)).build();
    //when

    //then
    Assertions.assertThatThrownBy(
            () -> validator.validate(buildOrderCreateRequest().build(), store, new HashMap<>()))
        .isInstanceOf(StoreNotOpenException.class)
        .hasMessage("가게 운영시간이 아닙니다.");
  }

  @DisplayName("주문항목이 비어있는 경우 실패한다.")
  @Test
  void validateOrderRequestMenuIsEmpty() {
    //given

    Store store = aShop()
        .openTime(LocalTime.now().minus(5, ChronoUnit.HOURS))
        .closeTime(LocalTime.now().plus(50, ChronoUnit.MINUTES)).build();
    //when

    //then
    Assertions.assertThatThrownBy(
            () -> validator.validate(buildOrderCreateRequest().menus(Arrays.asList()).build(), store,
                new HashMap<>()))
        .isInstanceOf(OrderEmptyException.class)
        .hasMessage("주문 항목이 비어있습니다.");
  }


  @DisplayName("최소 배달 금액 이하로 입력된 경우 예외가 발생한다.")
  @Test
  void validateOrderRequestDeliveryFeeIsLessThanMin() {
    //given

    Store store = aShop()
        .openTime(LocalTime.now().minus(5, ChronoUnit.HOURS))
        .closeTime(LocalTime.now().plus(50, ChronoUnit.MINUTES)).build();
    //when

    //then
    OrderCreateRequest orderCreateRequest = buildOrderCreateRequest()
        .deliveryFee(Money.MIN.minus(Money.wons(100)))
        .build();
    Assertions.assertThatThrownBy(
            () -> validator.validate(orderCreateRequest, store, new HashMap<>()))
        .isInstanceOf(OrderDeliveryFeeException.class)
        .hasMessage(String.format("배달료는 %s 이상 입력해주세요.", Money.MIN.wons()));
  }

  @DisplayName("주문 수량이 1개 이하이면 예외가 발생한다.")
  @Test
  void validateMenuCountlessThanOne() {
    //given
    Store store = aShop().build();
    //when
    OrderCreateRequest orderCreateRequest = buildOrderCreateRequest()
        .deliveryFee(Money.wons(1000))
        .menus(Arrays.asList(buildOrderMenuRequest().count(0).build()))
        .build();

    //then
    Assertions.assertThatThrownBy(
            () -> validator.validate(orderCreateRequest, store, new HashMap<>()))
        .isInstanceOf(OrderMenuCountException.class)
        .hasMessage("메뉴의 수량을 1개 이상 입력해주세요.");
  }

  @DisplayName("메뉴가 존재하지 않는 경우 예외가 발생한다.")
  @Test
  void validateMenuNotFound() {
    //given
    Store store = aShop().build();
    //when
    OrderCreateRequest orderCreateRequest = buildOrderCreateRequest()
        .deliveryFee(Money.wons(1000))
        .menus(Arrays.asList(buildOrderMenuRequest().count(1).build()))
        .build();

    //then
    Assertions.assertThatThrownBy(
            () -> validator.validate(orderCreateRequest, store, new HashMap<>()))
        .isInstanceOf(MenuNotFoundException.class)
        .hasMessage("메뉴가 존재하지 않습니다.");
  }

  @DisplayName("필수 옵션인 경우 선택되지 않으면 예외가 발생한다.")
  @Test
  void validateEssentialOption() {
    //given
    Store store = aShop().build();
    //when
    OrderCreateRequest orderCreateRequest = buildOrderCreateRequest()
        .deliveryFee(Money.wons(1000))
        .menus(Arrays.asList(
            buildOrderMenuRequest()
                .groups(Arrays.asList(
                    buildOrderGroupRequest()
                        .optionGroupId(1L)
                        .specs(new ArrayList<>())
                        .build())).build()))
        .build();
    //then
    Assertions.assertThatThrownBy(
            () -> validator.validate(orderCreateRequest, store, new HashMap<>() {{
              put(1L, aMenu().build());
            }}))
        .isInstanceOf(OrderGroupEmptyException.class)
        .hasMessage("필수 옵션을 선택해주세요.");
  }

  @DisplayName("다중 선택 옵션이 false 인 경우 2개 이상 선택되면 예외가 발생한다.")
  @Test
  void validateMultipleOption() {
    //given
    Store store = aShop().build();
    //when
    OrderCreateRequest orderCreateRequest = buildOrderCreateRequest()
        .deliveryFee(Money.wons(1000))
        .menus(Arrays.asList(
            buildOrderMenuRequest()
                .build())).build();
    //then
    Assertions.assertThatThrownBy(
            () -> validator.validate(orderCreateRequest, store, new HashMap<>() {{
              put(1L, aMenu()

                  .optionGroups(Arrays.asList(aOptionGroup()
                      .isMultipleOption(false)
                      .build()))
                  .build());
            }}))
        .isInstanceOf(OrderGroupIsNotMultipleException.class)
        .hasMessage("1개의 옵션만 선택 가능합니다.");
  }


  @DisplayName("검증 진행")
  @Test
  void validate() {
    //given
    Store store = aShop().build();
    //when
    OrderCreateRequest orderCreateRequest = buildOrderCreateRequest()
        .deliveryFee(Money.wons(1000))
        .menus(Arrays.asList(
            buildOrderMenuRequest()
                .build())).build();
    validator.validate(orderCreateRequest, store, new HashMap<>() {{
      put(1L, aMenu()

          .optionGroups(Arrays.asList(aOptionGroup()
              .build()))
          .build());
    }});
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