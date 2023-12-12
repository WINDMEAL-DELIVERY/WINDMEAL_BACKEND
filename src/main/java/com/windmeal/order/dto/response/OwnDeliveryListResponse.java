package com.windmeal.order.dto.response;

import com.windmeal.order.domain.DeliveryStatus;
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
@Schema(title = "내가 배달한 리스트 조회")
public class OwnDeliveryListResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long orderId;

  @Schema(description = "가게 ID", example = "1")
  private Long storeId;

  @Schema(description = "가게 이름", example = "컴포즈")
  private String storeName;

  @Schema(description = "가게 사진", example = "사진 주소")
  private String storePhoto;
  @Schema(description = "주문 요약", example = "황금올리브 외 1개 24000원")
  private String summary;

  @Schema(description = "배달 상태", example = "배달 완료")
  private String deliveryStatus;


  @Schema(description = "배달 날짜", example = "")
  private LocalDate deliveryDate;

  public OwnDeliveryListResponse(Long orderId, Long storeId, String storeName, String summary,
      DeliveryStatus deliveryStatus, LocalDateTime deliveryDate) {
    this.orderId = orderId;
    this.storeId = storeId;
    this.storeName = storeName;
    this.summary = summary;
    this.deliveryStatus = deliveryStatus.getStatus();
    this.deliveryDate = deliveryDate.toLocalDate();
  }
}
