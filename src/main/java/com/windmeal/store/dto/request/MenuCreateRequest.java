package com.windmeal.store.dto.request;


import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.MenuCategory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateRequest {
  private Long menuCategoryId;

  @NotBlank(message = "메뉴 이름을 입력해주세요.")
  private String name;
  private String description;

  @NotNull(message = "금액을 입력해주세요.")
  @Min(value=0, message="금액은 0원 이상이어야 합니다.")
  private int price;


  public Menu toEntity(MenuCategory menuCategory){
    return Menu.builder()
        .menuCategory(menuCategory)
        .name(this.name)
        .description(this.description)
        .price(Money.wons(this.price))
        .build();
  }
}
