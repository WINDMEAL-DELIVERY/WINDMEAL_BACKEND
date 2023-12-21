package com.windmeal.store.controller;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCategoryUpdateRequest;
import com.windmeal.store.dto.response.MenuCategoryResponse;
import com.windmeal.store.service.MenuCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 메뉴 카테고리 생성 메뉴 카테고리 이름 수정
 */
@Tag(name = "메뉴 카테고리", description = "메뉴 카테고리 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class MenuCategoryController {

  private final MenuCategoryService menuCategoryService;

  @Operation(summary = "메뉴 카테고리 생성 요청", description = "메뉴 카테고리가 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "CREATED"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PostMapping("/store/{storeId}/menuCategory")
  public ResultDataResponseDTO<MenuCategoryResponse> createMenuCategory(
      @Valid @RequestBody MenuCategoryCreateRequest request, @PathVariable Long storeId) {
    return ResultDataResponseDTO.of(menuCategoryService.createMenuCategory(request, storeId), ErrorCode.CREATED);
  }
  @Operation(summary = "메뉴 카테고리 수정 요청", description = "메뉴 카테고리가 수정됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PatchMapping("/menuCategory/{menuCategoryId}")
  public ResultDataResponseDTO updateMenuCategory(
      @Valid @RequestBody MenuCategoryUpdateRequest request,
      @PathVariable Long menuCategoryId) {
    menuCategoryService.updateMenuCategory(request, menuCategoryId);
    return ResultDataResponseDTO.empty();
  }
}
