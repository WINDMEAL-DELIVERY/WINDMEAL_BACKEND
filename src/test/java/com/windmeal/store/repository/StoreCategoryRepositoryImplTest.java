package com.windmeal.store.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.StoreCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

class StoreCategoryRepositoryImplTest extends IntegrationTestSupport {
  @Autowired
  private StoreCategoryJpaRepository storeCategoryRepository;

  @Autowired
  private CategoryJpaRepository categoryRepository;
  @Autowired
  private StoreJpaRepository storeJpaRepository;


  @AfterEach
  void tearDown() {
    storeCategoryRepository.deleteAllInBatch();
    categoryRepository.deleteAllInBatch();
    storeJpaRepository.deleteAllInBatch();
  }

  @DisplayName("store_category 에 category_id 와 store_id 에 매핑되어 생성한다.")
  @Test
  void createStoreCategories(){
    //given
    categoryRepository.save(Category.builder().name("커피챗").build());
    categoryRepository.save(Category.builder().name("커피").build());
    categoryRepository.save(Category.builder().name("카페").build());

    List<String> categoryNameList = new ArrayList<>();
    categoryNameList.add("커피");
    categoryNameList.add("카페");
    categoryNameList.add("커피챗");

    Store savedStore = storeJpaRepository.save(Store.builder().name("test").build());

    List<Category> categories = categoryRepository.findAllByNameIn(categoryNameList);

    List<Long> categoryId = categories.stream().map(Category::getId).collect(Collectors.toList());

    //when
    storeCategoryRepository.createStoreCategories(categoryId, savedStore.getId());

    //then
    List<StoreCategory> storeCategoryList = storeCategoryRepository.findAllByStoreId(savedStore.getId());
    Assertions.assertThat(storeCategoryList).hasSize(3);
  }
}