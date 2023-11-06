package com.windmeal.store.controller;

import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.CategoryCreateRequest;
import com.windmeal.store.dto.request.CategoryUpdateRequest;
import com.windmeal.store.dto.response.CategoryResponse;
import com.windmeal.store.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 카테고리 생성
 * 카테고리 수정
 * 카테고리 삭제
 */
@Tag(name = "가게 카테고리", description = "가게 카테고리 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * 카테고리 생성
   * @param request
   * @return
   */
  @Operation(summary = "가게 카테고리 생성 요청", description = "가게 카테고리가 생성됩니다.", tags = { "Category Controller" })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK",
          content = @Content(schema = @Schema(implementation = ResultDataResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
      @ApiResponse(responseCode = "404", description = "NOT FOUND"),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
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
