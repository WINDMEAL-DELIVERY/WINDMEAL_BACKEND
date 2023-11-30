package com.windmeal.store.dto.response;

import com.windmeal.model.place.Place;
import com.windmeal.store.domain.Store;
import com.windmeal.store.validator.StoreValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "가게 정보")
public class StoreResponse {


  @Schema(description = "가게 ID", example = "1")
  private Long storeId;
  @Schema(description = "가게 주인 ID", example = "1")
  private Long ownerId;
  @Schema(description = "가게 이름", example = "신의 한컵")
  private String name;
  @Schema(description = "가게 전화번호", example = "031-234-5678")
  private String phoneNumber;
  @Schema(description = "가게 사진 url", example = "http://test.s3/test")
  private String photo;
  @Schema(description = "가게 오픈 시간", example = "00:00:00")
  private LocalTime openTime;
  @Schema(description = "가게 종료 시간", example = "23:00:00")
  private LocalTime closeTime;

  @Schema(description = "장소 이름", example = "가천대학교")
  private String placeName;
  @Schema(description = "경도", example = "1.2345")
  private Double longitude;

  @Schema(description = "위도", example = "1.2345")
  private Double latitude;

  @Schema(description = "현재 가게 운영 여부", example = "true")
  private boolean isOpen;

  private List<String> categories;

  public static StoreResponse of(Store store, Place place) {
    return StoreResponse.builder()
        .storeId(store.getId())
        .ownerId(store.getOwner().getId())
        .name(store.getName())
        .phoneNumber(store.getPhoneNumber())
        .photo(store.getPhoto())
        .openTime(store.getOpenTime())
        .closeTime(store.getCloseTime())
        .placeName(place.getName())
        .latitude(place.getLatitude())
        .longitude(place.getLongitude())
        .isOpen(store.isOpen())
        .build();
  }
}
