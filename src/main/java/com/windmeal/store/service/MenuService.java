package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.exception.MenuCategoryNotFoundException;
import com.windmeal.store.repository.MenuCategoryJpaRepository;
import com.windmeal.store.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

  private final MenuJpaRepository menuRepository;
  private final MenuCategoryJpaRepository menuCategoryRepository;

  @Transactional
  public void createMenu(MenuCreateRequest request){
    MenuCategory menuCategory = menuCategoryRepository.findById(request.getMenuCategoryId())
        .orElseThrow(() -> new MenuCategoryNotFoundException(
            ErrorCode.NOT_FOUND, "메뉴 카테고리를 지정해주세요"));
    menuRepository.save(request.toEntity(menuCategory));
  }

}
