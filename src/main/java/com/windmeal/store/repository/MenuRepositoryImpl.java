package com.windmeal.store.repository;

import static com.windmeal.store.domain.QMenu.menu;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.dto.response.MenuResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<MenuResponse> findByMenuCategoryIdIn(List<Long> menuCategoryIds) {
    return jpaQueryFactory.select(Projections.constructor(
            MenuResponse.class,
            menu.id,
            menu.menuCategory.id,
            menu.name,
            menu.description,
            menu.price
        ))
        .from(menu)
        .where(menu.menuCategory.id.in(menuCategoryIds)).fetch();

  }
}
