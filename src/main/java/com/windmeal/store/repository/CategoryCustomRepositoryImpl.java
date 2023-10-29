package com.windmeal.store.repository;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void createCategories(List<String> categoryNameList) {
    String sql = "INSERT INTO category (name)\n"
        + "SELECT (?)\n"
        + "WHERE NOT EXISTS (\n"
        + "    SELECT 1 FROM category WHERE name = (?)\n"
        + ");";

    jdbcTemplate.batchUpdate(sql,
        categoryNameList,
        categoryNameList.size(),
        (PreparedStatement ps, String categoryName) -> {
          ps.setString(1, categoryName);
          ps.setString(2, categoryName);
        });
  }
}
