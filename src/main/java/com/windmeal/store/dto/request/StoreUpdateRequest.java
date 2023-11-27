package com.windmeal.store.dto.request;

import com.windmeal.model.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "가게 수정")
public class StoreUpdateRequest {

  @Schema(description = "가게 이름", example = "메가 커피")
  @NotBlank(message = "가게 이름은 빈칸일 수 없습니다.")
  private String name;

  @Schema(description = "가게 전화번호", example = "031-234-5678")
  private String phoneNumber;

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

  public Place toPlaceEntity() {
      return Place.builder()
          .name(this.placeName)
          .latitude(this.latitude)
          .longitude(this.longitude)
          .build();
    }
}
