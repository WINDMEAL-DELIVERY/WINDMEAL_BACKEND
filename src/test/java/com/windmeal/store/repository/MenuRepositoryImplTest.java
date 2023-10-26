package com.windmeal.store.repository;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.MenuCategoryResponse;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.service.MenuCategoryService;
import com.windmeal.store.service.MenuService;
import com.windmeal.store.service.StoreService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuRepositoryImplTest extends IntegrationTestSupport {

  @Autowired
  MenuJpaRepository menuJpaRepository;
  @Autowired
  MenuService menuService;

  @Autowired
  MenuCategoryService menuCategoryService;

  @Autowired
  StoreService storeService;
  @Autowired
  StoreJpaRepository storeJpaRepository;
  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MenuCategoryJpaRepository menuCategoryJpaRepository;

  @AfterEach
  void tearDown() {
    menuJpaRepository.deleteAllInBatch();
    menuCategoryJpaRepository.deleteAllInBatch();
    storeJpaRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("메뉴 카테고리 Id 값에 포함되는 menu를 불러온다.")
  @Test
  void findByMenuCategoryIdIn() {
    //given
    MenuCategoryResponse menuCategory1 = createMenuCategory();
    MenuCategoryResponse menuCategory2 = createMenuCategory();
    MenuCategoryResponse menuCategory3 = createMenuCategory();
    List<Long> menuCategoryIds = new ArrayList<>();
    menuCategoryIds.add(menuCategory1.getMenuCategoryId());
    menuCategoryIds.add(menuCategory2.getMenuCategoryId());

    String name = "name test";
    String description = "description test";
    int price = 1000;
    String imgUrl = "img url test";

    MenuResponse menu1 = createMenu(menuCategory1, name, description, price, imgUrl);
    MenuResponse menu2 = createMenu(menuCategory2, name, description, price, imgUrl);
    MenuResponse menu3 = createMenu(menuCategory3, name, description, price, imgUrl);

    //when
    List<MenuResponse> menus = menuJpaRepository.findByMenuCategoryIdIn(
        menuCategoryIds);

    //then
    Assertions.assertThat(menus).hasSize(2)
        .extracting(MenuResponse::getMenuId,MenuResponse::getMenuCategoryId)
        .containsExactlyInAnyOrder(
            tuple(menu1.getMenuId(),menuCategory1.getMenuCategoryId()),
            tuple(menu2.getMenuId(),menuCategory2.getMenuCategoryId())
        );
  }

  private MenuResponse createMenu(MenuCategoryResponse menuCategory1, String name, String description,
      int price, String imgUrl) {
    return menuService.createMenu(
        buildMenuCreateRequest(menuCategory1.getMenuCategoryId(), name, description,
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
        buildMenuCategoryCreateRequest(storeId, "test name1"));
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

  private MenuCategoryCreateRequest buildMenuCategoryCreateRequest(long storeId, String name) {
    return MenuCategoryCreateRequest
        .builder()
        .storeId(storeId)
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