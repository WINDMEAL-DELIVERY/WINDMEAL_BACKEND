package com.windmeal.store.repository;

import com.windmeal.store.domain.Menu;
import com.windmeal.store.dto.response.MenuResponse;
import java.util.List;

public interface MenuRepository {
  List<MenuResponse> findByMenuCategoryIdIn(List<Long> menuCategoryIds);
}
