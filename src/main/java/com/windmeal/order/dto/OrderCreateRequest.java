package com.windmeal.order.dto;

import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.store.domain.Store;
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
public class OrderCreateRequest {
  private Long storeId;
  private Long memberId;
  private Point destination;//도착지
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간
  private Money deliveryFee;//배달료
  private List<OrderMenuRequest> menus = new ArrayList<>();//주문한 메뉴 정보 리스트


  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderMenuRequest{
    private Long menuId;
    private int count;
    private Money price;
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
    private Long optionGroupId;
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
    private Long optionSpecId;
    private Money price;


  }
}
