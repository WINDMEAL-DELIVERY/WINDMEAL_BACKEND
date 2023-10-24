package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.request.MenuCategoryCreateRequest;
import com.windmeal.store.dto.request.MenuCategoryUpdateRequest;
import com.windmeal.store.exception.MenuCategoryNotFoundException;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.MenuCategoryJpaRepository;
import com.windmeal.store.repository.StoreJpaRepository;
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

  private final StoreJpaRepository storeRepository;
  private final MenuCategoryJpaRepository menuCategoryRepository;

  @Transactional
  public void createMenuCategory(MenuCategoryCreateRequest request) {
    Store store = storeRepository.findById(request.getStoreId())
        .orElseThrow(() -> new StoreNotFoundException(
            ErrorCode.NOT_FOUND, "가게가 존재하지 않습니다."));

    menuCategoryRepository.save(request.toEntity(store));
  }

  @Transactional
  public void updateMenuCategory(MenuCategoryUpdateRequest request) {
    MenuCategory menuCategory = menuCategoryRepository.findById(request.getMenuCategoryId())
        .orElseThrow(
            () -> new MenuCategoryNotFoundException(ErrorCode.NOT_FOUND, "메뉴 카테고리가 존재하지 않습니다."));

    menuCategory.updateName(request.getName());
  }
}
