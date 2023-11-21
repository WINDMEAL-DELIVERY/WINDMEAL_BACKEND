package com.windmeal.order.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "주문 생성 결과")
public class OrderCreateResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long id;

  @Schema(description = "도착지 주소", example = "{\"x\":1.234,\"y\":2.3456}")
  private Point destination;//도착지

  @Schema(description = "도착 예상 시간", example = "23:10:20")
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간

  @Schema(description = "배달료", example = "5000")
  private Money deliveryFee;//배달료

  @Schema(description = "총 금액", example = "15000")
  private Money totalPrice;//배달료

  @Schema(description = "내용 요약", example = "후라이드 치킨 1마리 외 3개 15000원")
  private String summary; //내용 요약 ex) 후라이드 치킨 1마리 외 3개 15000원

  public static OrderCreateResponse toResponse(Order order){
    return OrderCreateResponse.builder()
        .id(order.getId())
        .deliveryFee(order.getDeliveryFee())
        .totalPrice(order.getTotalPrice())
        .summary(order.getSummary())
        .destination(order.getDestination())
        .eta(order.getEta())
        .build();

  }
}
