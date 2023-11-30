package com.windmeal.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "주문 삭제")
public class OrderDeleteRequest {
  @Schema(description = "주문 ID", example = "1")
  private Long orderId;
  @Schema(description = "주문자 ID", example = "1")
  private Long memberId;
}
