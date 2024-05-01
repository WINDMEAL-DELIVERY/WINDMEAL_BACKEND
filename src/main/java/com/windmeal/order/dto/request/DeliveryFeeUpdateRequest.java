package com.windmeal.order.dto.request;

import com.windmeal.generic.domain.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "배달료 수정")
public class DeliveryFeeUpdateRequest {
    @Schema(description = "주문 ID", example = "1")
    private Long orderId;
    @Schema(description = "배달료", example = "5000")
    private Money deliveryFee;
}
