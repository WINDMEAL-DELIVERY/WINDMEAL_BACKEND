package com.windmeal.store.repository;

import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.store.domain.Category;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

class CategoryRepositoryImplTest extends IntegrationTestSupport {

  @Autowired
  private CategoryJpaRepository categoryRepository;

  @AfterEach
  void tearDown() {
    categoryRepository.deleteAllInBatch();
  }

  @DisplayName("카테고리 이름 리스트가 입력되면 없는 경우 생성한다.")
  @Test
  void createCategories() {
    //given
    categoryRepository.save(Category.builder().name("커피챗").build());
    categoryRepository.save(Category.builder().name("이름").build());

    List<String> categoryNameList = new ArrayList<>();
    categoryNameList.add("커피");
    categoryNameList.add("카페");
    categoryNameList.add("커피챗");

    //when
    categoryRepository.createCategories(categoryNameList);

    //then
    List<Category> all = categoryRepository.findAllByNameIn(categoryNameList);
    assertThat(all).hasSize(3)
        .extracting("name")
        .containsExactlyInAnyOrder("커피","카페","커피챗");
  }
}