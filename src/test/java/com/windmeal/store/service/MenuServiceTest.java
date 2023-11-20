package com.windmeal.store.service;

import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.MenuCategoryResponse;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.MenuCategoryNotFoundException;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.StoreRepository;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTestSupport {

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

  @AfterEach
  void tearDown() {
    menuJpaRepository.deleteAllInBatch();
    menuCategoryRepository.deleteAllInBatch();
    storeRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("메뉴를 생성한다.")
  @Test
  void createMenu() {
    //given
    MenuCategoryResponse menuCategory = createMenuCategory();

    Long menuCategoryId = menuCategory.getMenuCategoryId();
    String name = "name test";
    String description = "description test";
    int price = 1000;
    String imgUrl = "img url test";

    //when
    MenuResponse menu = menuService.createMenu(
        buildMenuCreateRequest(menuCategoryId, name, description,
            price), imgUrl);

    //then
    Menu findMenu = menuJpaRepository.findById(menu.getMenuId()).orElse(null);

    assertThat(findMenu.getName()).isEqualTo(name);
    assertThat(findMenu.getDescription()).isEqualTo(description);
    assertThat(findMenu.getPrice().wons()).isEqualTo((price));
    assertThat(findMenu.getPhoto()).isEqualTo(imgUrl);
  }


  @DisplayName("메뉴를 생성 시 메뉴 카테고리가 존재하지 않으면 예외가 발생한다.")
  @Test
  void createMenuWithMenuCategoryNotFound() {
    //given
    MenuCategoryResponse menuCategory = createMenuCategory();

    Long menuCategoryId = 0L;
    String name = "name test";
    String description = "description test";
    int price = 1000;
    String imgUrl = "img url test";

    //when

    //then
    assertThatThrownBy(() -> menuService.createMenu(
        buildMenuCreateRequest(menuCategoryId, name, description,
            price), imgUrl))
        .isInstanceOf(MenuCategoryNotFoundException.class)
        .hasMessage("메뉴 카테고리를 지정해주세요");

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
        buildMenuCategoryCreateRequest( "test name1"),storeId);
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