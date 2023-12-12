package com.windmeal.order.dto.response;

import com.windmeal.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "내가 주문한 리스트 조회")
public class OwnOrderListResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long orderId;

  @Schema(description = "가게 ID", example = "1")
  private Long storeId;

  @Schema(description = "가게 이름", example = "컴포즈")
  private String storeName;

  @Schema(description = "주문 요약", example = "황금올리브 외 1개 24000원")
  private String summary;

  @Schema(description = "주문 상태", example = "배달 완료")
  private String orderStatus;

  @Schema(description = "주문 날짜", example = "")
  private LocalDate orderDate;

  public OwnOrderListResponse(Long orderId, Long storeId, String storeName, String summary,
      OrderStatus orderStatus, LocalDateTime orderDate) {
    this.orderId = orderId;
    this.storeId = storeId;
    this.storeName = storeName;
    this.summary = summary;
    this.orderStatus = orderStatus.getStatus();
    this.orderDate = orderDate.toLocalDate();
  }
}
