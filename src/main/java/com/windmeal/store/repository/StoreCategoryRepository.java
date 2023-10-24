package com.windmeal.store.repository;

import java.util.List;

public interface StoreCategoryRepository {

  void createStoreCategories(List<Long> categoryIdList, Long storeId);

  void createStoreCategoriesNotExist(List<Long> categoryIdList, Long storeId);
}
