
package com.windmeal.store.dto.request;

import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "메뉴 카테고리 수정 ex)인기 메뉴, 커피, 음료 등")
public class MenuCategoryUpdateRequest {

  @Schema(description = "메뉴 카테고리 이름", example = "인기 메뉴")
  @NotBlank(message = "카테고리명은 빈칸이 될 수 없습니다.")
  private String name;

}
