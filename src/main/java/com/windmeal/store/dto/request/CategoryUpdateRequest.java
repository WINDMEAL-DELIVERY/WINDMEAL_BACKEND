package com.windmeal.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "카테고리 수정 (가게 검색을 위한 카테고리 ex)커피,컵밥 등)")
public class CategoryUpdateRequest {
  @Schema(description = "카테고리 ID", example = "1")
  private Long id;
  @Schema(description = "카테고리 이름", example = "카페")
  @NotBlank(message = "카테고리명은 빈칸이 될 수 없습니다.")
  private String name;
}
