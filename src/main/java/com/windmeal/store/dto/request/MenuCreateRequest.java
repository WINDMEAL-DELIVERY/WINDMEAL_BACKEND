package com.windmeal.store.dto.request;


import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.MenuCategory;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "메뉴 생성")
public class MenuCreateRequest {
  @Schema(description = "메뉴 카테고리 ID", example = "1")
  private Long menuCategoryId;

  @Schema(description = "메뉴 이름", example = "불닭 마요")
  @NotBlank(message = "메뉴 이름을 입력해주세요.")
  private String name;

  @Schema(description = "메뉴 상세 내용", example = "닭고기와 불닭 마요 소스가 들어간 덮밥입니다.")
  private String description;

  @Schema(description = "메뉴 가격", example = "5000")
  @NotNull(message = "금액을 입력해주세요.")
  @Min(value = 0, message = "금액은 0원 이상이어야 합니다.")
  private int price;


  public Menu toEntity(MenuCategory menuCategory,String imageUrl) {
    return Menu.builder()
        .menuCategory(menuCategory)
        .name(this.name)
        .description(this.description)
        .price(Money.wons(this.price))
        .photo(imageUrl)
        .build();
  }
}
