package com.windmeal.store.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MenuResponse {
  private Long menuId;
  private Long menuCategoryId;
  private String name;
  private String description;
  private Money price;
  private String photo;

  public static MenuResponse of(Menu menu){
    return MenuResponse.builder()
        .menuId(menu.getId())
        .name(menu.getName())
        .description(menu.getDescription())
        .price(menu.getPrice())
        .photo(menu.getPhoto())
        .build();
  }
}
