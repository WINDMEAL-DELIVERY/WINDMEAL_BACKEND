package com.windmeal.order.validator;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.windmeal.generic.domain.Money;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderMenuRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import com.windmeal.order.exception.OrderDeliveryFeeException;
import com.windmeal.order.exception.OrderEmptyException;
import com.windmeal.order.exception.OrderGroupEmptyException;
import com.windmeal.order.exception.OrderGroupIsNotMultipleException;
import com.windmeal.order.exception.OrderMenuCountException;
import com.windmeal.order.exception.OrderSpecificationChangeException;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.domain.Store;
import com.windmeal.store.exception.MenuChangeException;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.exception.StoreNotOpenException;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderValidator {

  private final StoreRepository storeRepository;
  private final MenuRepository menuRepository;

  private final MemberRepository memberRepository;

  public void validate(OrderCreateRequest request) {
    validate(request,getStore(request), getMenus(getMenuIds(request.getMenus())));
  }

  private Map<Long, Menu> getMenus(List<Long> menuIds) {
    return menuRepository.findAllById(menuIds).stream().collect(toMap(Menu::getId, identity()));
  }

  void validate(OrderCreateRequest request,Store store, Map<Long, Menu> menu) {
    if (!store.isOpen()) {
      throw new StoreNotOpenException(ErrorCode.BAD_REQUEST, "가게 운영시간이 아닙니다.");
    }

    if (request.getMenus().isEmpty()) {
      throw new OrderEmptyException(ErrorCode.BAD_REQUEST, "주문 항목이 비어있습니다.");
    }

    if(request.getDeliveryFee().isLessThan(Money.MIN)){
      throw new OrderDeliveryFeeException(ErrorCode.BAD_REQUEST,String.format("배달료는 %s 이상 입력해주세요.",Money.MIN.wons()));
    }
    for (OrderMenuRequest requestMenu : request.getMenus()) {
      validateMenu(requestMenu, menu.getOrDefault(requestMenu.getMenuId(), null));
    }
  }

  private void validateMenu(OrderMenuRequest requestMenu, Menu menu) {
    if (requestMenu.getCount() <= 0) {
      throw new OrderMenuCountException(ErrorCode.BAD_REQUEST, "메뉴의 수량을 1개 이상 입력해주세요.");
    }
    if (menu == null) {
      throw new MenuNotFoundException(ErrorCode.BAD_REQUEST, "메뉴가 존재하지 않습니다.");
    }

    if(!(menu.getName().equals(requestMenu.getName())&&menu.getPrice().wons()==requestMenu.getPrice().wons())){
      throw new MenuChangeException(ErrorCode.BAD_REQUEST, "메뉴가 변경되었습니다.");
    }
    Map<Long, List<OrderSpecRequest>> menuGroups = getMenuGroups(requestMenu);
    for (OptionGroup optionGroup : menu.getOptionGroups()) {
      isGroupSatisfied(optionGroup, menuGroups.getOrDefault(optionGroup.getId(), new ArrayList<>()));
    }
  }

  private static Map<Long, List<OrderSpecRequest>> getMenuGroups(OrderMenuRequest requestMenu) {
   return  requestMenu.getGroups().stream()
        .collect(Collectors.toMap(OrderGroupRequest::getOptionGroupId, OrderGroupRequest::getSpecs));

  }

  private void isGroupSatisfied(OptionGroup optionGroup, List<OrderSpecRequest> optionSpecs) {

    //필수 옵션인데 1개도 선택되지 않은 경우
    if(optionGroup.getIsEssentialOption() && optionSpecs.isEmpty()){
      throw new OrderGroupEmptyException(ErrorCode.BAD_REQUEST,"필수 옵션을 선택해주세요.");
    }
    //다중 선택 옵션이 없는데 1개 이상 선택한 경우
    if(!optionGroup.getIsMultipleOption()&& optionSpecs.size()>1){
      throw new OrderGroupIsNotMultipleException(ErrorCode.BAD_REQUEST,"1개의 옵션만 선택 가능합니다.");
    }

    if(satisfied(optionGroup.getOptionSpecifications(), optionSpecs).size()!=optionSpecs.size()){
      throw new OrderSpecificationChangeException(ErrorCode.BAD_REQUEST,"메뉴 상세 정보가 변경되었습니다.");
    }

  }


  private Store getStore(OrderCreateRequest request) {
    return storeRepository.findById(request.getStoreId())
        .orElseThrow(() -> new StoreNotFoundException(
            ErrorCode.NOT_FOUND, "존재하지 않는 매장입니다."));
  }

  private Member getMember(OrderCreateRequest request) {
    return memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));
  }
  private List<Long> getMenuIds(List<OrderMenuRequest> menus) {
    return menus.stream().map(OrderMenuRequest::getMenuId).collect(toList());
  }

  private List<OrderSpecRequest> satisfied(List<OptionSpecification> optionSpecs,List<OrderSpecRequest> options) {
    return optionSpecs
        .stream()
        .flatMap(spec -> options.stream().filter(spec::isSatisfiedBy))
        .collect(toList());
  }
}
