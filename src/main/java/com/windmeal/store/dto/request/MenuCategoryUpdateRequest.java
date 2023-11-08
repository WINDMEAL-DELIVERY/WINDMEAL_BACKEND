
package com.windmeal.store.dto.request;

import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategoryUpdateRequest {

  @NotBlank(message = "카테고리명은 빈칸이 될 수 없습니다.")
  private String name;

}
