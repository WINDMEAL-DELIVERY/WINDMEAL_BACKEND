package com.windmeal.store.repository;

import com.windmeal.store.domain.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

  OptionGroup findByMenuId(Long menuId);
}
