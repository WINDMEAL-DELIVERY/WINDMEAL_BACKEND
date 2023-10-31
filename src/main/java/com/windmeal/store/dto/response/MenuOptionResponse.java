package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MenuOptionResponse {

  private MenuResponse menuResponse;
  private List<OptionGroupResponse> optionGroups;

  public MenuOptionResponse(Menu menu, List<OptionGroup> optionGroups,
      Map<Long, List<OptionSpecResponse>> optionSpecifications) {
    this.menuResponse = MenuResponse.of(menu);
    this.optionGroups = optionGroups.stream()
        .map(optionGroup -> OptionGroupResponse.of(optionGroup,
            optionSpecifications.get(optionGroup.getId()))).collect(Collectors.toList());
  }
}
