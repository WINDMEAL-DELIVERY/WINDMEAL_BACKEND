package com.windmeal.order.dto.response;

import com.windmeal.generic.domain.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(title = "주문 리스트 조회")
public class OrderListResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long id;

  @Schema(description = "도착지 주소", example = "{\"x\":1.234,\"y\":2.3456}")
  private Point destination;//도착지

  @Schema(description = "도착 예상 시간", example = "23:10:20")
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간

  @Schema(description = "배달료", example = "5000")
  private Money deliveryFee;//배달료

  @Schema(description = "식당 이름", example = "불닭 마요 덮밥")
  private String name;

  @Schema(description = "주문 요약", example = "불닭 마요 덮밥 외 1개 2000원")
  private String summary;


  public OrderListResponse(Long id, Point destination, LocalDateTime eta, Money deliveryFee,
      String name, String summary) {
    this.id = id;
    this.destination = destination;
    this.eta = eta.toLocalTime();
    this.deliveryFee = deliveryFee;
    this.name = name;
    this.summary = summary;
  }
}
