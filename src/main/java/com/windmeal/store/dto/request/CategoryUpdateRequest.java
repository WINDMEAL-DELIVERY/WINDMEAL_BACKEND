package com.windmeal.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {

    private Long id;

    @NotBlank(message = "카테고리명은 빈칸이 될 수 없습니다.")
    private String name;
}
