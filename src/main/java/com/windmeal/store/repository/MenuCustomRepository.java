package com.windmeal.store.repository;

import com.windmeal.store.dto.response.MenuResponse;
import java.util.List;

public interface MenuCustomRepository {
  List<MenuResponse> findByMenuCategoryIdIn(List<Long> menuCategoryIds);
}
