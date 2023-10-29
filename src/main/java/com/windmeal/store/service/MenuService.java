package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.exception.MenuCategoryNotFoundException;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

  private final MenuRepository menuRepository;
  private final MenuCategoryRepository menuCategoryRepository;

  @Transactional
  public MenuResponse createMenu(MenuCreateRequest request,String imageUrl) {
    MenuCategory menuCategory = menuCategoryRepository.findById(request.getMenuCategoryId())
        .orElseThrow(() -> new MenuCategoryNotFoundException(
            ErrorCode.NOT_FOUND, "메뉴 카테고리를 지정해주세요"));
    Menu savedMenu = menuRepository.save(request.toEntity(menuCategory, imageUrl));
    return MenuResponse.of(savedMenu);
  }

}
