package com.windmeal.store.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.request.OptionCreateRequest;
import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.MenuCategoryResponse;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.OptionGroupRepository;
import com.windmeal.store.repository.OptionSpecificationRepository;
import com.windmeal.store.repository.StoreRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OptionServiceTest extends IntegrationTestSupport {

  @Autowired
  OptionService optionService;

  @Autowired
  MenuService menuService;

  @Autowired
  MenuCategoryService menuCategoryService;

  @Autowired
  StoreService storeService;
  @Autowired
  StoreRepository storeRepository;
  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MenuCategoryRepository menuCategoryRepository;

  @Autowired
  MenuRepository menuJpaRepository;

  @Autowired
  OptionSpecificationRepository optionSpecificationJpaRepository;

  @Autowired
  OptionGroupRepository optionGroupRepository;

  @AfterEach
  void tearDown() {
    optionSpecificationJpaRepository.deleteAllInBatch();
    optionGroupRepository.deleteAllInBatch();
    menuJpaRepository.deleteAllInBatch();
    menuCategoryRepository.deleteAllInBatch();
    storeRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("옵션 그룹 및 상세 내용을 생성한다.")
  @Test
  void createOption() {
    //given
    MenuResponse menu = createMenu();

    //when
    OptionCreateRequest optionCreateRequest = buildOptionCreateRequest("name", true, true,
        getOptionSpecs());
    optionService.createOption(optionCreateRequest,menu.getMenuId());
    //then
    OptionGroup optionGroup = optionGroupRepository.findByMenuId(menu.getMenuId());
    assertThat(optionGroup)
        .extracting(OptionGroup::getName,OptionGroup::isEssentialOption,OptionGroup::isMultipleOption)
        .containsExactly("name",true,true);

    List<OptionSpecification> optionSpecs = optionSpecificationJpaRepository.findByOptionGroupId(
        optionGroup.getId());
    assertThat(optionSpecs)
        .extracting(optionSpecification -> optionSpecification.getName(),
            optionSpecification1 -> optionSpecification1.getPrice().wons())
        .containsExactlyInAnyOrder(
            tuple("name1",1000),
            tuple("name2",2000)
        );
  }

  @DisplayName("옵션 그룹 및 상세 내용을 생성한다.")
  @Test
  void createOptionEmptySpec() {
    //given
    MenuResponse menu = createMenu();

    //when
    OptionCreateRequest optionCreateRequestEmptySpec = buildOptionCreateRequest("name", true, true,
        Arrays.asList());
    optionService.createOption(optionCreateRequestEmptySpec,menu.getMenuId());
    //then
    OptionGroup optionGroup = optionGroupRepository.findByMenuId(menu.getMenuId());
    assertThat(optionGroup)
        .extracting(OptionGroup::getName,OptionGroup::isEssentialOption,OptionGroup::isMultipleOption)
        .containsExactly("name",true,true);

    List<OptionSpecification> optionSpecs = optionSpecificationJpaRepository.findByOptionGroupId(
        optionGroup.getId());
    assertThat(optionSpecs)
        .extracting(optionSpecification -> optionSpecification.getName(),
            optionSpecification1 -> optionSpecification1.getPrice().wons())
        .containsExactlyInAnyOrder(
        );
  }
  @DisplayName("옵션 그룹 및 상세 내용을 생성할 때 메뉴가 존재하지 않으면 예외가 발생한다.")
  @Test
  void createOptionWithMenuNotFound() {
    //given
    MenuResponse menu = createMenu();

    //when
    OptionCreateRequest optionCreateRequest = buildOptionCreateRequest("name", true, true,
        getOptionSpecs());

    //then
    assertThatThrownBy(() -> optionService.createOption(optionCreateRequest,0L))
        .isInstanceOf(MenuNotFoundException.class)
        .hasMessage("메뉴가 존재하지 않습니다.");

  }

  private static OptionCreateRequest buildOptionCreateRequest(String name,
      boolean isEssentialOption,
      boolean isMultipleOption, List<OptionSpecRequest> optionSpecs) {
    return OptionCreateRequest.builder()
        .name(name)
        .isEssentialOption(isEssentialOption)
        .isMultipleOption(isMultipleOption)
        .optionSpec(optionSpecs).build();
  }

  private static List<OptionSpecRequest> getOptionSpecs() {
    return Arrays.asList(buildOptionSpecRequest("name1", 1000),
        buildOptionSpecRequest("name2", 2000));
  }

  private static OptionSpecRequest buildOptionSpecRequest(String name1, int price) {
    return OptionSpecRequest.builder()
        .name(name1)
        .price(price).build();
  }

  private MenuResponse createMenu() {
    MenuCategoryResponse menuCategory = createMenuCategory();
    Long menuCategoryId = menuCategory.getMenuCategoryId();
    String name = "name test";
    String description = "description test";
    int price = 1000;
    String imgUrl = "img url test";

    return menuService.createMenu(
        buildMenuCreateRequest(menuCategoryId, name, description,
            price), imgUrl);
  }


  private MenuCategoryResponse createMenuCategory() {
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);

    StoreResponse response = storeService.createStore(request, "photo");
    Long storeId = response.getStoreId();

    //when
    return menuCategoryService.createMenuCategory(
        buildMenuCategoryCreateRequest("test name1"), storeId);
  }

  private MenuCreateRequest buildMenuCreateRequest(long menuCategoryId, String name,
      String description, int price) {
    return MenuCreateRequest
        .builder()
        .menuCategoryId(menuCategoryId)
        .name(name)
        .description(description)
        .price(price)
        .build();
  }

  private MenuCategoryCreateRequest buildMenuCategoryCreateRequest(String name) {
    return MenuCategoryCreateRequest
        .builder()
        .name(name)
        .build();
  }

  private StoreCreateRequest createStoreCreateRequest(
      Member savedMember, String phoneNumber, String name, LocalTime openTime, LocalTime closeTime,
      Double lat, Double lon) {
    return StoreCreateRequest.builder()
        .memberId(savedMember.getId())
        .name(name)
        .phoneNumber(phoneNumber)
        .memberId(savedMember.getId())
        .openTime(openTime)
        .closeTime(closeTime)
        .latitude(lat)
        .longitude(lon)
        .categoryList(new ArrayList<>())
        .build();
  }

  private Member createMember(String name) {
    return memberRepository.save(Member.builder()
        .name(name).build());
  }
}