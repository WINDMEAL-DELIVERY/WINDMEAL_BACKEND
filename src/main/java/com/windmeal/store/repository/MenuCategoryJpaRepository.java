package com.windmeal.store.repository;

import com.windmeal.store.domain.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryJpaRepository extends JpaRepository<MenuCategory, Long> {

}
