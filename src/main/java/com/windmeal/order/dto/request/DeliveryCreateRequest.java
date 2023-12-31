package com.windmeal.order.dto.request;

import com.windmeal.order.domain.Delivery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "배달 요청 생성")
public class DeliveryCreateRequest {

  @Schema(description = "주문 ID", example = "1")
  private Long orderId;
  @Schema(description = "배달원 ID", example = "1")
  private Long memberId;


  public DeliveryCreateRequest toServiceDto(Long currentMemberId) {
    this.memberId=currentMemberId;
    return this;
  }
}
