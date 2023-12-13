package com.windmeal.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "가게 카테고리 생성")
public class StoreCategoryCreateRequest {

  private Long storeId;

  @Schema(description = "카테고리 이름", example = "카페")
  @NotBlank(message = "카테고리명은 빈칸이 될 수 없습니다.")
  private String name;

  public StoreCategoryCreateRequest toServiceDto(Long storeId) {
    this.storeId=storeId;
    return this;
  }
}
