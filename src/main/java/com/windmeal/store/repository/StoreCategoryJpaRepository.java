package com.windmeal.store.repository;

import com.windmeal.store.domain.StoreCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory, Long>,
    StoreCategoryRepository {

  List<StoreCategory> findAllByStoreId(Long storeId);
}
