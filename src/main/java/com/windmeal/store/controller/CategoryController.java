package com.windmeal.store.controller;

import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.CategoryCreateRequest;
import com.windmeal.store.dto.request.CategoryUpdateRequest;
import com.windmeal.store.dto.response.CategoryResponse;
import com.windmeal.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping("/category")
  public ResultDataResponseDTO createCategory(@Valid @RequestBody CategoryCreateRequest request) {

    CategoryResponse response = categoryService.createCategory(request);

    return ResultDataResponseDTO.of(response);
  }

  @PatchMapping("/category")
  public ResultDataResponseDTO updateCategory(@RequestBody CategoryUpdateRequest request) {

    categoryService.updateCategory(request);

    return ResultDataResponseDTO.empty();
  }

  @DeleteMapping("/category")
  public ResultDataResponseDTO deleteCategory(@RequestBody CategoryUpdateRequest request) {

    categoryService.deleteCategory(request);

    return ResultDataResponseDTO.empty();
  }
}
