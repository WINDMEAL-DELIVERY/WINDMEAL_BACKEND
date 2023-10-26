package com.windmeal.store.repository;

import java.util.List;

public interface CategoryRepository {

  void createCategories(List<String> categoryNameList);
}
