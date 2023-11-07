package com.windmeal.store.repository;

import com.windmeal.store.domain.OptionGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

  List<OptionGroup> findByMenuId(Long menuId);
}
