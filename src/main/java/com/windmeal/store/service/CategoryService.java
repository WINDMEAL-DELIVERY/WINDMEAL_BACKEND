package com.windmeal.store.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.Category;
import com.windmeal.store.dto.request.CategoryCreateRequest;
import com.windmeal.store.dto.request.CategoryUpdateRequest;
import com.windmeal.store.dto.response.CategoryResponse;
import com.windmeal.store.exception.CategoryNotFoundException;
import com.windmeal.store.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 카테고리 생성
 * 카테고리 삭제
 * 카테고리 수정
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

  private final CategoryRepository categoryJpaRepository;

  @Transactional
  public CategoryResponse createCategory(CategoryCreateRequest request) {
    Category savedCategory = categoryJpaRepository.save(request.toEntity());
    return CategoryResponse.of(savedCategory);
  }

  @Transactional
  public void updateCategory(CategoryUpdateRequest request) {
    Category category = categoryJpaRepository.findById(request.getId()).orElseThrow(
        () -> new CategoryNotFoundException());
    category.updateName(request.getName());
  }

  @Transactional
  public void deleteCategory(CategoryUpdateRequest request) {
    categoryJpaRepository.findById(request.getId()).orElseThrow(
        () -> new CategoryNotFoundException());
    categoryJpaRepository.deleteById(request.getId());
  }

}
