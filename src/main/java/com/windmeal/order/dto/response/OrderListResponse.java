package com.windmeal.order.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.windmeal.generic.domain.Money;
import com.windmeal.generic.domain.MoneyDeserializer;
import com.windmeal.generic.domain.MoneySerializer;
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
@Schema(title = "주문 리스트 조회")
public class OrderListResponse {

  @Schema(description = "주문 ID", example = "1")
  private Long id;

  @Schema(description = "주문자 ID", example = "1")
  private Long memberId;

  @Schema(description = "주문자 닉네임", example = "dong")
  private String memberNickName;

  @Schema(description = "장소 이름", example = "가천대학교")
  private String placeName;
  @Schema(description = "경도", example = "1.2345")
  private Double longitude;
  @Schema(description = "위도", example = "1.2345")
  private Double latitude;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime orderTime;

  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  @Schema(description = "도착 예상 시간", example = "23:10:20")
  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간

//  @JsonSerialize(using = MoneySerializer.class)
//  @JsonDeserialize(using = MoneyDeserializer.class)
  @Schema(description = "배달료", example = "5000")
  private Money deliveryFee;//배달료

  @Schema(description = "식당 이름", example = "불닭 마요 덮밥")
  private String name;

  @Schema(description = "주문 요약", example = "불닭 마요 덮밥 외 1개 2000원")
  private String summary;


  public OrderListResponse(Long id, Long memberId, String memberNickName, String placeName,
      Double longitude, Double latitude, LocalDateTime orderTime, LocalDateTime eta, Money deliveryFee, String name,
      String summary) {
    this.id = id;
    this.memberId = memberId;
    this.memberNickName = memberNickName;
    this.placeName = placeName;
    this.longitude = longitude;
    this.latitude = latitude;
    this.orderTime = orderTime;
    this.eta = eta.toLocalTime();
    this.deliveryFee = deliveryFee;
    this.name = name;
    this.summary = summary;
  }
}
