package com.windmeal.store.service;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.store.domain.Category;
import com.windmeal.store.dto.request.CategoryCreateRequest;
import com.windmeal.store.dto.request.CategoryUpdateRequest;
import com.windmeal.store.dto.response.CategoryResponse;
import com.windmeal.store.exception.CategoryNotFoundException;
import com.windmeal.store.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class CategoryServiceTest extends IntegrationTestSupport {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryJpaRepository;

    @AfterEach
    void tearDown() {
        categoryJpaRepository.deleteAllInBatch();
    }

    @DisplayName("신규 카테고리를 등록한다.")
    @Test
    void createCategory(){
        //given
        CategoryCreateRequest request = createCategoryCreateRequest("커피");

        //when
        CategoryResponse response = categoryService.createCategory(request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
    }


    @DisplayName("카테고리 이름을 수정한다.")
    @Test
    void updateCategory(){
        //given
        Category savedCategory = createCategory(createCategoryCreateRequest("커피"));

        String updateName = "업데이트 커피";
        //when
        CategoryUpdateRequest updateRequest = createCategoryUpdateRequest(savedCategory.getId(),updateName );
        categoryService.updateCategory(updateRequest);

        //then
        Category findCategory = categoryJpaRepository.findById(updateRequest.getId()).get();

        assertThat(findCategory).extracting("id","name")
                        .containsExactly(updateRequest.getId(),updateName);
    }

    @DisplayName("카테고리 이름을 수정할 때 카테고리가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void updateCategoryWithNotFoundCategory(){
        //given
        String updateName = "업데이트 커피";

        //when

        //then
        CategoryUpdateRequest updateRequest = createCategoryUpdateRequest(0L,updateName );

        assertThatThrownBy(() -> categoryService.updateCategory(updateRequest))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("카테고리가 존재하지 않습니다.");
    }

    @DisplayName("카테고리를 삭제한다.")
    @Test
    void deleteCategory(){
        //given
        Category savedCategory = createCategory(createCategoryCreateRequest("커피"));

        //when
        CategoryUpdateRequest updateRequest = createCategoryUpdateRequest(savedCategory.getId(), savedCategory.getName());
        categoryService.deleteCategory(updateRequest);

        //then
        Category findCategory = categoryJpaRepository.findById(savedCategory.getId()).orElse(null);
        assertThat(findCategory)
                .isNull();
    }

    @DisplayName("카테고리를 삭제할 때 해당 카테고리가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void deleteCategoryWithNotFoundCategory(){
        //given

        //when
        CategoryUpdateRequest updateRequest = createCategoryUpdateRequest(0L, "test");


        //then
        assertThatThrownBy(() -> categoryService.deleteCategory(updateRequest))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("카테고리가 존재하지 않습니다.");
    }

    private Category createCategoryBuild(String name) {
        return Category.builder()
                .name(name)
                .build();
    }

    private CategoryCreateRequest createCategoryCreateRequest(String name) {
        return CategoryCreateRequest.builder()
                .name(name)
                .build();
    }

    private CategoryUpdateRequest createCategoryUpdateRequest(Long id,String name) {
        return CategoryUpdateRequest.builder()
                .id(id)
                .name(name)
                .build();
    }

    private Category createCategory(CategoryCreateRequest request) {
        return categoryJpaRepository.save(request.toEntity());
    }
}