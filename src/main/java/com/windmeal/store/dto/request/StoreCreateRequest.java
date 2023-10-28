package com.windmeal.store.dto.request;

import com.windmeal.member.domain.Member;
import com.windmeal.store.domain.Store;
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
public class StoreCreateRequest {

  private Long memberId;

  @NotBlank(message = "가게 이름은 빈칸일 수 없습니다.")
  private String name;

  private String phoneNumber;

  private LocalTime openTime;

  private LocalTime closeTime;

  private Double longitude;

  private Double latitude;

  private List<String> categoryList = new ArrayList<>();

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
