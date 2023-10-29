package com.windmeal.store.service;

import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCategoryUpdateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.MenuCategoryResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.MenuCategoryNotFoundException;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.StoreRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuCategoryServiceTest extends IntegrationTestSupport {

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


  @AfterEach
  void tearDown() {
    menuCategoryRepository.deleteAllInBatch();
    storeRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @DisplayName("메뉴 카테고리를 생성한다.")
  @Test
  void createMenuCategory() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);

    StoreResponse response = storeService.createStore(request, "photo");
    Long storeId = response.getStoreId();

    //when
    menuCategoryService.createMenuCategory(buildMenuCategoryCreateRequest( "test name1"),storeId);
    menuCategoryService.createMenuCategory(buildMenuCategoryCreateRequest( "test name2"),storeId);
    //then
    List<MenuCategory> menuCategories = menuCategoryRepository.findByStoreId(storeId);
    assertThat(menuCategories).hasSize(2)
        .extracting("name")
        .containsExactlyInAnyOrder("test name1", "test name2");
  }


  @DisplayName("메뉴 카테고리를 생성 시 가게가 없는 경우 예외가 발생한다.")
  @Test
  void createMenuCategoryWithStoreNotFound() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);

    StoreResponse response = storeService.createStore(request, "photo");
    Long storeId = 0L;

    //when

    //then
    assertThatThrownBy(
        () -> menuCategoryService.createMenuCategory(
            buildMenuCategoryCreateRequest("test name1"),storeId))
        .isInstanceOf(StoreNotFoundException.class)
        .hasMessage("가게가 존재하지 않습니다.");
  }

  @DisplayName("메뉴 카테고리 이름을 변경한다.")
  @Test
  void updateMenuCategory() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);

    StoreResponse response = storeService.createStore(request, "photo");
    Long storeId = response.getStoreId();

    //when
    String originalName = "test name1";
    MenuCategoryResponse menuCategory = menuCategoryService.createMenuCategory(
        buildMenuCategoryCreateRequest(originalName),storeId);
    Long menuCategoryId = menuCategory.getMenuCategoryId();

    String newName = "test new name";
    menuCategoryService.updateMenuCategory(buildMenuCategoryUpdateRequest(newName),menuCategoryId);

    //then
    MenuCategory findMenuCategory = menuCategoryRepository.findById(menuCategoryId).get();
    assertThat(findMenuCategory.getName()).isEqualTo(newName);

  }

  @DisplayName("메뉴 카테고리 이름을 변경 시 메뉴 카테고리가 존재하지 않으면 예외가 발생한다.")
  @Test
  void updateMenuCategoryWithMenuCategoryNotFound() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);

    StoreResponse response = storeService.createStore(request, "photo");
    Long storeId = response.getStoreId();

    //when
    String originalName = "test name1";
    MenuCategoryResponse menuCategory = menuCategoryService.createMenuCategory(
        buildMenuCategoryCreateRequest(originalName),storeId);
    Long menuCategoryId = 0L;

    String newName = "test new name";

    //then
    assertThatThrownBy(() -> menuCategoryService.updateMenuCategory(
        buildMenuCategoryUpdateRequest(newName),menuCategoryId))
        .isInstanceOf(MenuCategoryNotFoundException.class)
        .hasMessage("메뉴 카테고리가 존재하지 않습니다.");
  }

  private MenuCategoryCreateRequest buildMenuCategoryCreateRequest(String name) {
    return MenuCategoryCreateRequest
        .builder()
        .name(name)
        .build();
  }

  private MenuCategoryUpdateRequest buildMenuCategoryUpdateRequest(
      String name) {
    return MenuCategoryUpdateRequest
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