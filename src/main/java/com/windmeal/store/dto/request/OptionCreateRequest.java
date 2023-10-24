package com.windmeal.store.dto.request;


import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionCreateRequest {

  private Long menuId;

  private String name;

  private boolean isEssentialOption; //필수 선택 여부

  private boolean isMultipleOption; //다중 선택 여부

  private List<OptionSpecRequest> optionSpec;

  @Getter
  public class OptionSpecRequest {
    private String name;
    private int price;
  }

  public OptionGroup toOptionGroupEntity(Menu menu){
    return OptionGroup.builder()
        .menu(menu)
        .name(this.name)
        .isEssentialOption(this.isEssentialOption)
        .isMultipleOption(this.isMultipleOption)
        .build();
  }

}
