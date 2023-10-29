package com.windmeal.store.dto.request;


import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionCreateRequest {
  @NotBlank(message = "옵션 명은 빈칸이 될 수 없습니다.")
  private String name;
  @NotNull(message = "필수 선택 여부를 선택해주세요.")
  private Boolean isEssentialOption; //필수 선택 여부
  @NotNull(message = "다중 선택 여부를 선택해주세요.")
  private Boolean isMultipleOption; //다중 선택 여부

  private List<OptionSpecRequest> optionSpec = new ArrayList<>();

  @Getter
  @Builder
  @AllArgsConstructor
  public static class OptionSpecRequest {

    private String name;
    private int price;
  }

  public OptionGroup toOptionGroupEntity(Menu menu) {
    return OptionGroup.builder()
        .menu(menu)
        .name(this.name)
        .isEssentialOption(this.isEssentialOption)
        .isMultipleOption(this.isMultipleOption)
        .build();
  }

}
