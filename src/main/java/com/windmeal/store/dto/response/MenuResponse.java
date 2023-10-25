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
  public MenuDateResponse toResponseData(){
    return new MenuDateResponse(this);
  }
  @Getter
  public static class MenuDateResponse{

    private Long menuId;
    private String name;
    private String description;
    private int price;
    private String photo;
    private MenuDateResponse(MenuResponse menuResponse) {
      this.menuId = menuResponse.getMenuId();
      this.name=menuResponse.getName();
      this.description=menuResponse.getDescription();
      this.price=menuResponse.getPrice().wons();
      this.photo=menuResponse.getPhoto();
    }
  }

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
