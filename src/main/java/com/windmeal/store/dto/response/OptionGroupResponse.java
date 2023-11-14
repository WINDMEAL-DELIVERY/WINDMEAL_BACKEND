package com.windmeal.store.dto.response;

import com.windmeal.store.domain.OptionGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(title = "옵션 정보")
public class OptionGroupResponse {
  @Schema(description = "옵션 그룹 ID", example = "1")
  private Long optionGroupId;
  @Schema(description = "옵션 이름", example = "맛 선택")
  private String name;
  @Schema(description = "필수 선택 여부", example = "true")
  private Boolean isEssentialOption; //필수 선택 여부
  @Schema(description = "다중 선택 여부", example = "true")
  private Boolean isMultipleOption; //다중 선택 여부

  private List<OptionSpecResponse> optionSpecs;

  public static OptionGroupResponse of(OptionGroup optionGroup,List<OptionSpecResponse> optionSpecs) {
    return OptionGroupResponse.builder()
        .optionGroupId(optionGroup.getId())
        .name(optionGroup.getName())
        .isEssentialOption(optionGroup.getIsEssentialOption())
        .isMultipleOption(optionGroup.getIsMultipleOption())
        .optionSpecs(optionSpecs)
        .build();
  }
}
