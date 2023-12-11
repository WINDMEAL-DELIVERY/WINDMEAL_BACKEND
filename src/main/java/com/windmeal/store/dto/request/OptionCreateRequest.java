package com.windmeal.store.dto.request;


import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "옵션 생성")
public class OptionCreateRequest {

  @Schema(description = "옵션 이름", example = "맛 선택")
  @NotBlank(message = "옵션 명은 빈칸이 될 수 없습니다.")
  private String name;
  @Schema(description = "필수 선택 여부", example = "true")
  @NotNull(message = "필수 선택 여부를 선택해주세요.")
  private Boolean isEssentialOption; //필수 선택 여부

  @Schema(description = "다중 선택 여부", example = "true")
  @NotNull(message = "다중 선택 여부를 선택해주세요.")
  private Boolean isMultipleOption; //다중 선택 여부


  private List<OptionSpecRequest> optionSpec;

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(title = "옵션 상세 내용")
  public static class OptionSpecRequest {

    @Schema(description = "옵션 상세 이름", example = "밥 추가")
    private String name;
    @Schema(description = "옵션 상세 가격", example = "1000")
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
