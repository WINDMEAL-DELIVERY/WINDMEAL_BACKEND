package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.response.MenuResponse.MenuDateResponse;
import com.windmeal.store.validator.StoreValidator;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StoreMenuResponse {

  private Long storeId;

  private String name;

  private String phoneNumber;

  private String photo;
  private LocalTime openTime;

  private LocalTime closeTime;

  private Point location;

  private boolean isOpen;

  private List<MenuCategoryResponse> menuCategories;

  public StoreMenuResponse(Store store, List<MenuCategory> menuCategories, List<MenuResponse> menus,
      StoreValidator storeValidator) {

    this.storeId = store.getId();
    this.name = store.getName();
    this.phoneNumber = store.getPhoneNumber();
    this.photo = store.getPhoto();
    this.openTime = store.getOpenTime();
    this.closeTime = store.getCloseTime();
    this.location = store.getLocation();
    this.isOpen = storeValidator.validateStoreIsOpen(store.getOpenTime(), store.getCloseTime(),
        LocalTime.now());
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
  private class MenuCategoryResponse {

    private Long menuCategoryId;
    private String name;
    private List<MenuDateResponse> menus;

    private MenuCategoryResponse(MenuCategory menuCategory, List<MenuResponse> menus) {
      this.menuCategoryId = menuCategory.getId();
      this.name = menuCategory.getName();
      this.menus = menus.stream().map(MenuResponse::toResponseData).collect(Collectors.toList());
    }
  }


}
