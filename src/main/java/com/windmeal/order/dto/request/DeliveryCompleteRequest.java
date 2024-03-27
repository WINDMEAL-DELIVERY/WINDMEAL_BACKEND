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
@Schema(title = "배달 요청 완료")
public class DeliveryCompleteRequest {
    @Schema(description = "주문 ID", example = "1")
    private Long orderId;
    @Schema(description = "배달원 ID", example = "1")
    private Long memberId;
}
