package com.windmeal.store.dto.response;

import com.windmeal.store.domain.OptionGroup;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OptionGroupResponse {
  private Long optionGroupId;
  private String name;

  private Boolean isEssentialOption; //필수 선택 여부

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
