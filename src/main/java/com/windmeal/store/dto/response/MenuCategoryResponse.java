package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.MenuCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MenuCategoryResponse {

  private Long menuCategoryId;

  private Long storeId;

  private String name;

  public static MenuCategoryResponse of(MenuCategory menuCategory,Long storeId) {
    return MenuCategoryResponse.builder()
        .menuCategoryId(menuCategory.getId())
        .name(menuCategory.getName())
        .storeId(storeId)
        .build();
  }
}
