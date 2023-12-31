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
import com.windmeal.order.exception.OrderGroupValidException;
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
import java.util.Set;
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
      throw new StoreNotOpenException();
    }

    if (request.getMenus().isEmpty()) {
      throw new OrderEmptyException();
    }

    if(request.getDeliveryFee().isLessThan(Money.MIN)){
      throw new OrderDeliveryFeeException(String.format("배달료는 %s 이상 입력해주세요.",Money.MIN.wons()));
    }
    for (OrderMenuRequest requestMenu : request.getMenus()) {
      validateMenu(requestMenu, menu.getOrDefault(requestMenu.getMenuId(), null));
    }
  }

  private void validateMenu(OrderMenuRequest requestMenu, Menu menu) {
    if (requestMenu.getCount() <= 0) {
      throw new OrderMenuCountException();
    }
    if (menu == null) {
      throw new MenuNotFoundException();
    }

    if(!(menu.getName().equals(requestMenu.getName())&&menu.getPrice().wons()==requestMenu.getPrice().wons())){
      throw new MenuChangeException();
    }

    Map<Long, List<OrderSpecRequest>> menuGroups = getMenuGroups(requestMenu);
    List<OptionGroup> optionGroups = menu.getOptionGroups();

    isGroupValid(menuGroups.keySet(),optionGroups.stream().map(OptionGroup::getId).collect(toList()));

    for (OptionGroup optionGroup : optionGroups) {
      isGroupSatisfied(optionGroup, menuGroups.getOrDefault(optionGroup.getId(), new ArrayList<>()));
    }
  }

  private void isGroupValid(Set<Long> keySet, List<Long> collect) {
    keySet.stream()
        .filter(key -> !collect.contains(key))
        .findFirst()
        .ifPresent(key -> {
          throw new OrderGroupValidException();
        });
  }


  private static Map<Long, List<OrderSpecRequest>> getMenuGroups(OrderMenuRequest requestMenu) {
   return  requestMenu.getGroups().stream()
        .collect(Collectors.toMap(OrderGroupRequest::getOptionGroupId, OrderGroupRequest::getSpecs));

  }

  private void isGroupSatisfied(OptionGroup optionGroup, List<OrderSpecRequest> optionSpecs) {

    //필수 옵션인데 1개도 선택되지 않은 경우
    if(optionGroup.getIsEssentialOption() && optionSpecs.isEmpty()){
      throw new OrderGroupEmptyException();
    }
    //다중 선택 옵션이 없는데 1개 이상 선택한 경우
    if(!optionGroup.getIsMultipleOption()&& optionSpecs.size()>1){
      throw new OrderGroupIsNotMultipleException();
    }

    if(satisfied(optionGroup.getOptionSpecifications(), optionSpecs).size()!=optionSpecs.size()){
      throw new OrderSpecificationChangeException();
    }

  }


  private Store getStore(OrderCreateRequest request) {
    return storeRepository.findById(request.getStoreId())
        .orElseThrow(() -> new StoreNotFoundException());
  }

  private Member getMember(OrderCreateRequest request) {
    return memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException());
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
