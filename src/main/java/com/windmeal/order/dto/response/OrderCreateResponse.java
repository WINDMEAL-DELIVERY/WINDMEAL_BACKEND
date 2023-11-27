package com.windmeal.order.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.model.place.Place;
import com.windmeal.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "주문 생성 결과")
public class OrderCreateResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long id;

  @Schema(description = "장소 이름", example = "가천대학교")
  private String placeName;
  @Schema(description = "경도", example = "1.2345")
  private Double longitude;
  @Schema(description = "위도", example = "1.2345")
  private Double latitude;

  @Schema(description = "도착 예상 시간", example = "23:10:20")
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간

  @Schema(description = "배달료", example = "5000")
  private Money deliveryFee;//배달료

  @Schema(description = "총 금액", example = "15000")
  private Money totalPrice;//배달료

  @Schema(description = "내용 요약", example = "후라이드 치킨 1마리 외 3개 15000원")
  private String summary; //내용 요약 ex) 후라이드 치킨 1마리 외 3개 15000원

  public static OrderCreateResponse toResponse(Order order, Place place){
    return OrderCreateResponse.builder()
        .id(order.getId())
        .deliveryFee(order.getDeliveryFee())
        .totalPrice(order.getTotalPrice())
        .summary(order.getSummary())
        .placeName(place.getName())
        .latitude(place.getLatitude())
        .longitude(place.getLongitude())
        .eta(order.getEta().toLocalTime())
        .build();

  }
}
