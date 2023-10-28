package com.windmeal.store.repository;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class StoreCategoryRepositoryImpl implements StoreCategoryRepository {

  private final JdbcTemplate jdbcTemplate;


  @Override
  public void createStoreCategories(List<Long> categoryIdList, Long storeId) {
    String sql = "INSERT INTO store_category (category_id,store_id)\n"
        + "VALUES (?, ?)";

    jdbcTemplate.batchUpdate(sql,
        categoryIdList,
        categoryIdList.size(),
        (PreparedStatement ps, Long categoryId) -> {
          ps.setLong(1, categoryId);
          ps.setLong(2, storeId);
        });
  }

  @Override
  public void createStoreCategoriesNotExist(List<Long> categoryIdList, Long storeId) {
    String sql = "INSERT INTO store_category (category_id,store_id)\n"
        + "SELECT (?),(?)\n"
        + "WHERE NOT EXISTS (\n"
        + "    SELECT 1 FROM store_category WHERE category_id = (?) and store_id = (?)\n"
        + ");";

    jdbcTemplate.batchUpdate(sql,
        categoryIdList,
        categoryIdList.size(),
        (PreparedStatement ps, Long categoryId) -> {
          ps.setLong(1, categoryId);
          ps.setLong(2, storeId);
          ps.setLong(3, categoryId);
          ps.setLong(4, storeId);
        });
  }
}
