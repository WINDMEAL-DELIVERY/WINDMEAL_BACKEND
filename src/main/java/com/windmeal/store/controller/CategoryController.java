package com.windmeal.store.controller;

import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.CategoryCreateRequest;
import com.windmeal.store.dto.request.CategoryUpdateRequest;
import com.windmeal.store.dto.response.CategoryResponse;
import com.windmeal.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 카테고리 생성
 * 카테고리 수정
 * 카테고리 삭제
 */
@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * 카테고리 생성
   * @param request
   * @return
   */
  @PostMapping("/category")
  public ResultDataResponseDTO createCategory(@Valid @RequestBody CategoryCreateRequest request) {

    CategoryResponse response = categoryService.createCategory(request);

    return ResultDataResponseDTO.of(response);
  }

  /**
   * 카테고리 수정
   * @param request
   * @return
   */
  @PatchMapping("/category")
  public ResultDataResponseDTO updateCategory(@RequestBody CategoryUpdateRequest request) {

    categoryService.updateCategory(request);

    return ResultDataResponseDTO.empty();
  }

  /**
   * 카테고리 삭제
   * @param request
   * @return
   */
  @DeleteMapping("/category")
  public ResultDataResponseDTO deleteCategory(@RequestBody CategoryUpdateRequest request) {

    categoryService.deleteCategory(request);

    return ResultDataResponseDTO.empty();
  }
}
