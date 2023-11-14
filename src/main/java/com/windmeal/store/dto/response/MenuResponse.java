package com.windmeal.store.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "메뉴 정보")
public class MenuResponse {
  @Schema(description = "메뉴 ID", example = "1")
  private Long menuId;
  @Schema(description = "메뉴 카테고리 ID", example = "1")
  private Long menuCategoryId;
  @Schema(description = "메뉴 이름", example = "불닭 마요 덮밥")
  private String name;
  @Schema(description = "메뉴 상세 내용", example = "닭고기와 불닭 마요 소스가 들어간 덮밥입니다.")
  private String description;
  @Schema(description = "메뉴 가격", example = "5000")
  private Money price;
  @Schema(description = "메뉴 사진 url", example = "http://test.s3/test")
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
