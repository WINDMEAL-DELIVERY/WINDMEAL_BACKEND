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
@Schema(title = "가게 정보")
public class AllStoreResponse {
  @Schema(description = "가게 ID", example = "1")
  private Long storeId;

  @Schema(description = "가게 이름", example = "신의 한컵")
  private String name;
}
