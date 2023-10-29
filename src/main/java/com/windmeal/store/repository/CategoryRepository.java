package com.windmeal.store.repository;

import com.windmeal.store.domain.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long>,
    CategoryCustomRepository {

  List<Category> findAllByNameIn(List<String> categoryName);
}
