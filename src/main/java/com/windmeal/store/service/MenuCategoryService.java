package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCategoryUpdateRequest;
import com.windmeal.store.dto.response.MenuCategoryResponse;
import com.windmeal.store.exception.MenuCategoryNotFoundException;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 메뉴 카테고리 생성 메뉴 카테고리 이름 수정
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuCategoryService {

  private final StoreRepository storeRepository;
  private final MenuCategoryRepository menuCategoryRepository;

  @Transactional
  public MenuCategoryResponse createMenuCategory(MenuCategoryCreateRequest request,Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new StoreNotFoundException(
            ErrorCode.NOT_FOUND, "가게가 존재하지 않습니다."));

    MenuCategory savedMenuCategory = menuCategoryRepository.save(request.toEntity(store));
    return MenuCategoryResponse.of(savedMenuCategory,store.getId());
  }

  @Transactional
  public void updateMenuCategory(MenuCategoryUpdateRequest request, Long menuCategoryId) {


    MenuCategory menuCategory = menuCategoryRepository.findById(menuCategoryId)
        .orElseThrow(
            () -> new MenuCategoryNotFoundException(ErrorCode.NOT_FOUND, "메뉴 카테고리가 존재하지 않습니다."));

    menuCategory.updateName(request.getName());
  }
}
