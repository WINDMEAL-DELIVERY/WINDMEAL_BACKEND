package com.windmeal.order.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderGroupRequest;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "주문 상세 내용 조회")
public class OrderDetailResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long id;

  @Schema(description = "도착지 주소", example = "{\"x\":1.234,\"y\":2.3456}")
  private Point destination;//도착지

  @Schema(description = "도착 예상 시간", example = "23:10:20")
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간

  @Schema(description = "배달료", example = "5000")
  private Money deliveryFee;//배달료

  @Schema(description = "총 금액", example = "15000")
  private Money totalPrice;//총 금액

  @Schema(description = "주문 상태", example = "ORDERED")
  private OrderStatus orderStatus;//주문 상태


  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderMenuRequest{
    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;
    @Schema(description = "수량", example = "3")
    private int count;

    @Schema(description = "메뉴 이름", example = "불닭 마요 덮밥")
    private String name;

    @Schema(description = "메뉴 가격", example = "3000")
    private Money price;
    private List<OrderGroupRequest> groups = new ArrayList<>();

  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderGroupRequest{
    @Schema(description = "옵션 그룹 ID", example = "1")
    private Long optionGroupId;

    @Schema(description = "옵션 그룹 이름", example = "맛 선택")
    private String name;
    private List<OrderSpecRequest> specs = new ArrayList<>();

  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderSpecRequest{
    @Schema(description = "옵션 상세 ID", example = "1")
    private Long optionSpecId;

    @Schema(description = "옵션 상세 이름", example = "덜 매운맛")
    private String name;

    @Schema(description = "옵션 가격", example = "1000")
    private Money price;

  }

}
