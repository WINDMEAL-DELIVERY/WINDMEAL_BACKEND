package com.windmeal.store.dto.response;

import com.windmeal.model.place.Place;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "가게 정보")
public class StoreMenuResponse {


  @Schema(description = "가게 ID", example = "1")
  private Long storeId;
  @Schema(description = "가게 이름", example = "신의 한컵")
  private String name;
  @Schema(description = "가게 전화번호", example = "031-234-5678")
  private String phoneNumber;
  @Schema(description = "가게 사진 url", example = "http://test.s3/test")
  private String photo;
  @Schema(description = "가게 오픈 시간", example = "00:00:00")
  private LocalTime openTime;
  @Schema(description = "가게 종료 시간", example = "23:00:00")
  private LocalTime closeTime;

  @Schema(description = "장소 이름", example = "가천대학교")
  private String placeName;
  @Schema(description = "경도", example = "1.2345")
  private Double longitude;

  @Schema(description = "위도", example = "1.2345")
  private Double latitude;
  @Schema(description = "현재 가게 운영 여부", example = "true")
  private boolean isOpen;

  private List<MenuCategoryResponse> menuCategories;

  public StoreMenuResponse(Store store, List<MenuCategory> menuCategories, List<MenuResponse> menus,
      Place place) {

    this.storeId = store.getId();
    this.name = store.getName();
    this.phoneNumber = store.getPhoneNumber();
    this.photo = store.getPhoto();
    this.openTime = store.getOpenTime();
    this.closeTime = store.getCloseTime();
    this.placeName = place.getName();
    this.longitude = place.getLongitude();
    this.latitude = place.getLatitude();
    this.isOpen = store.isOpen();
    this.menuCategories = toMenuCategories(menuCategories, menus);
  }

  private List<MenuCategoryResponse> toMenuCategories(List<MenuCategory> menuCategories,
      List<MenuResponse> menus) {

    Map<Long, List<MenuResponse>> menuMap = menus.stream()
        .collect(Collectors.groupingBy(menu -> menu.getMenuCategoryId()));

    return menuCategories.stream().map(
            menuCategory -> new MenuCategoryResponse(menuCategory, menuMap.getOrDefault(menuCategory.getId(),new ArrayList<>())))
        .collect(Collectors.toList());
  }

  @Getter
  @Schema(title = "가게 메뉴 정보")
  private class MenuCategoryResponse {
    @Schema(description = "메뉴 카테고리 ID", example = "1")
    private Long menuCategoryId;
    @Schema(description = "메뉴 카테고리 이름", example = "인기 상품")
    private String name;
    private List<MenuResponse> menus;

    private MenuCategoryResponse(MenuCategory menuCategory, List<MenuResponse> menus) {
      this.menuCategoryId = menuCategory.getId();
      this.name = menuCategory.getName();
      this.menus = menus;
    }
  }


}
