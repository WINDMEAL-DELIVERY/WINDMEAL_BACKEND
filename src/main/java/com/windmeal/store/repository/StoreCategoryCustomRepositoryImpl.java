package com.windmeal.store.repository;

import static com.windmeal.store.domain.QCategory.category;
import static com.windmeal.store.domain.QStoreCategory.storeCategory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.store.domain.QCategory;
import com.windmeal.store.dto.response.StoreCategoryResponse;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class StoreCategoryCustomRepositoryImpl implements StoreCategoryCustomRepository {

  private final JdbcTemplate jdbcTemplate;

  private final JPAQueryFactory jpaQueryFactory;

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

  @Override
  public List<StoreCategoryResponse> getStoreCategories(Long storeId) {
    return jpaQueryFactory.select(
            Projections.constructor(StoreCategoryResponse.class,
                category.id,
                category.name,
                storeCategory.id)
        )
        .from(storeCategory)
        .leftJoin(category).on(storeCategory.category.id.eq(category.id))
        .where(storeCategory.store.id.eq(storeId)).fetch();

  }
}
