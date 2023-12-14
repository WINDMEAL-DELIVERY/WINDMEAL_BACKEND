package com.windmeal.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StoreCategoryResponse {

  @Schema(description = "카테고리 ID", example = "1")
  private Long categoryId;

  @Schema(description = "카테고리 이름", example = "카페")
  private String name;

  @Schema(description = "가게 카테고리 ID", example = "1")
  private Long storeCategoryId;
}
