package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryResponse {

  private Long id;

  private String name;

  public static CategoryResponse of(Category category) {
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .build();
  }
}
