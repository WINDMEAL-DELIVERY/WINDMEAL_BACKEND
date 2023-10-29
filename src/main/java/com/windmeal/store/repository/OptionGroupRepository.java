package com.windmeal.store.repository;

import com.windmeal.store.domain.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

  OptionGroup findByMenuId(Long menuId);
}
