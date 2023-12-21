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
@Schema(title = "주문 취소")
public class DeliveryCancelRequest {

  @Schema(description = "주문 ID", example = "1")
  private Long orderId;
  @Schema(description = "취소 요청자 ID", example = "1")
  private Long memberId;
  @Schema(description = "취소 사유", example = "~한 이유로 취소해야할 것 같습니다..")
  private String content;

  public DeliveryCancelRequest toServiceDto(Long memberId) {
    this.memberId = memberId;
    return this;
  }
}
