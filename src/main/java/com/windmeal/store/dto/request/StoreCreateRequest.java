package com.windmeal.store.dto.request;

import com.windmeal.member.domain.Member;
import com.windmeal.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "가게 생성")
public class StoreCreateRequest {
  @Schema(description = "가게 생성자 ID", example = "1")
  private Long memberId;

  @Schema(description = "가게 이름", example = "메가 커피")
  @NotBlank(message = "가게 이름은 빈칸일 수 없습니다.")
  private String name;

  @Schema(description = "가게 전화번호", example = "031-234-5678")
  private String phoneNumber;

  @Schema(description = "가게 오픈 시간", example = "00:00:00")
  private LocalTime openTime;
  @Schema(description = "가게 종료 시간", example = "23:00:00")
  private LocalTime closeTime;

  @Schema(description = "경도", example = "1.2345")
  private Double longitude;

  @Schema(description = "위도", example = "1.2345")
  private Double latitude;

  @Schema(description = "카테고리 이름(검색을 위한 카테고리)", example = "카페")
  private List<String> categoryList;

  public Store toEntity(Member member, String imgUrl) {
    return Store.builder()
        .owner(member)
        .name(this.name)
        .phoneNumber(this.phoneNumber)
        .photo(imgUrl)
        .openTime(this.openTime)
        .closeTime(this.closeTime)
        .location(new Point(latitude, longitude))
        .build();
  }

}
