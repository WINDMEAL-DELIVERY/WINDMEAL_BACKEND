package com.windmeal.store.repository;

import com.windmeal.store.domain.MenuCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryJpaRepository extends JpaRepository<MenuCategory, Long> {

  List<MenuCategory> findByStoreId(Long storeId);
}
