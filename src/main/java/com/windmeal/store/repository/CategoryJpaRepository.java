package com.windmeal.store.repository;

import com.windmeal.store.domain.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long>, CategoryRepository {

  List<Category> findAllByNameIn(List<String> categoryName);
}
