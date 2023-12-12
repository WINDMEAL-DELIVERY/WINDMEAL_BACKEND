package com.windmeal.store.repository;

import com.windmeal.store.dto.response.StoreCategoryResponse;
import java.util.List;

public interface StoreCategoryCustomRepository {

  void createStoreCategories(List<Long> categoryIdList, Long storeId);

  void createStoreCategoriesNotExist(List<Long> categoryIdList, Long storeId);

  List<StoreCategoryResponse> getStoreCategories(Long storeId);
}
