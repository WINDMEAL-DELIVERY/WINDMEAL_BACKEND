package com.windmeal.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "주문 정보(지도 표시)")
public class OrderMapListResponse {

  @Schema(description = "가게 ID", example = "1")
  private Long storeId;

  @Schema(description = "가게 이름", example = "컴포즈 커피")
  private String storeName;

  @Schema(description = "주문 수", example = "23")
  private Long orderCount;

  @Schema(description = "경도", example = "1.2345")
  private Double longitude;

  @Schema(description = "위도", example = "1.2345")
  private Double latitude;
}
