package com.windmeal.order.dto.response;

import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "내가 요청한 주문 목록")
public class OrderingListResponse {
  @Schema(description = "주문 ID", example = "1")
  private Long orderId;
  @Schema(description = "주문 상태", example = "배달 중")
  private String orderStatus;
  @Schema(description = "주문 요약", example = "불닭 마요 덮밥 외 1개 2000원")
  private String summary;
  @Schema(description = "추가 설명", example = "지하 1층 소웨 과실로 와주세요")
  private String description;
  @Schema(description = "장소 이름", example = "가천대학교")
  private String destinationName;
  @Schema(description = "닉네임", example = "임동동")
  private String nickName;
  @Schema(description = "배달기사 프로필 이미지", example = "profile_image/Windmeal_Delivery_default_profile_image.png")
  private String deliverProfileImage;
  @Schema(description = "음식점 이름", example = "신의한컵")
  private String storeName;

  public OrderingListResponse(
      Long orderId, OrderStatus orderStatus, String summary,
      String description, String destinationName, String deliverNickname, String deliverProfileImage, String storeName) {
    this.orderId = orderId;
    this.orderStatus = orderStatus.getStatus();
    this.summary = summary;
    this.description = description;
    this.destinationName = destinationName;
    this.nickName = deliverNickname;
    this.deliverProfileImage = deliverProfileImage;
    this.storeName = storeName;
  }
}
