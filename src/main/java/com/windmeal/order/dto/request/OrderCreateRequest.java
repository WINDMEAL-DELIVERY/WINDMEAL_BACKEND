package com.windmeal.order.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.model.place.Place;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "주문 생성")
public class OrderCreateRequest {
  @Schema(description = "가게 ID", example = "1")
  private Long storeId;
  @Schema(description = "주문자 ID", example = "1")
  private Long memberId;
  @Schema(description = "장소 이름", example = "가천대학교")
  private String placeName;
  @Schema(description = "경도", example = "1.2345")
  private Double longitude;
  @Schema(description = "위도", example = "1.2345")
  private Double latitude;
  @Schema(description = "추가 설명", example = "지하 1층 소웨 과실로 와주세요")
  private String description;
  @Schema(description = "도착 예상 시간", example = "23:10:20")
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간
  @Schema(description = "배달료", example = "5000")
  private Money deliveryFee;//배달료
  private List<OrderMenuRequest> menus = new ArrayList<>();//주문한 메뉴 정보 리스트
  public Place toPlaceEntity(){
    return Place.builder()
        .name(this.placeName)
        .latitude(this.latitude)
        .longitude(this.longitude)
        .build();
  }

  public OrderCreateRequest toServiceDto(Long memberId) {
    this.memberId = memberId;
    return this;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderMenuRequest{
    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;
    @Schema(description = "수량", example = "3")
    private int count;
    @Schema(description = "메뉴 가격", example = "3000")
    private Money price;
    private String name;

    private List<OrderGroupRequest> groups = new ArrayList<>();

    public Money calculatePrice() {
      return Money.sum(groups, OrderGroupRequest::calculatePrice).plus(price).times(count);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderGroupRequest{
    @Schema(description = "옵션 그룹 ID", example = "1")
    private Long optionGroupId;
    private String name;

    private List<OrderSpecRequest> specs = new ArrayList<>();
    public Money calculatePrice() {
      return Money.sum(specs, OrderSpecRequest::getPrice);
    }
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderSpecRequest{
    @Schema(description = "옵션 상세 ID", example = "1")
    private Long optionSpecId;
    @Schema(description = "옵션 가격", example = "1000")
    private Money price;
    private String name;

  }
}
