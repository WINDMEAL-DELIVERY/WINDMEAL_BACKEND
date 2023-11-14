package com.windmeal.store.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.OptionSpecification;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "옵션 상세 정보")
public class OptionSpecResponse {
  @Schema(description = "옵션 그룹 ID", example = "1")
  private Long optionGroupId;
  @Schema(description = "옵션 상세 ID", example = "1")
  private Long optionSpecId;
  @Schema(description = "옵션 상세 이름", example = "매운맛")
  private String name;
  @Schema(description = "옵션 상세 가격", example = "1000")
  private Money price;


}
