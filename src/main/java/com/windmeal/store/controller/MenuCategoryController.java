package com.windmeal.store.controller;


import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCategoryUpdateRequest;
import com.windmeal.store.service.MenuCategoryService;
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
@RestController
@RequiredArgsConstructor
public class MenuCategoryController {

  private final MenuCategoryService menuCategoryService;

  @PostMapping("/store/{storeId}/menuCategory")
  public ResultDataResponseDTO createMenuCategory(
      @Valid @RequestBody MenuCategoryCreateRequest request, @PathVariable Long storeId) {
    return ResultDataResponseDTO.of(menuCategoryService.createMenuCategory(request, storeId));
  }

  @PatchMapping("/menuCategory/{menuCategoryId}")
  public ResultDataResponseDTO updateMenuCategory(
      @Valid @RequestBody MenuCategoryUpdateRequest request,
      @PathVariable Long menuCategoryId) {
    menuCategoryService.updateMenuCategory(request, menuCategoryId);
    return ResultDataResponseDTO.empty();
  }
}
