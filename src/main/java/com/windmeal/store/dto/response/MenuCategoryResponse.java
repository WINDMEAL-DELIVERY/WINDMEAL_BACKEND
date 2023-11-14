package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.MenuCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "메뉴 카테고리 ")
public class MenuCategoryResponse {
  @Schema(description = "메뉴 카테고리 ID", example = "1")
  private Long menuCategoryId;
  @Schema(description = "가게 ID", example = "1")
  private Long storeId;
  @Schema(description = "메뉴 카테고리 이름", example = "인기 메뉴")
  private String name;

  public static MenuCategoryResponse of(MenuCategory menuCategory,Long storeId) {
    return MenuCategoryResponse.builder()
        .menuCategoryId(menuCategory.getId())
        .name(menuCategory.getName())
        .storeId(storeId)
        .build();
  }
}
