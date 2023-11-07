package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "카테고리 생성")
public class CategoryResponse {


  @Schema(description = "카테고리 ID", example = "1")
  private Long id;

  @Schema(description = "카테고리 이름", example = "카페")
  private String name;

  public static CategoryResponse of(Category category) {
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .build();
  }
}
