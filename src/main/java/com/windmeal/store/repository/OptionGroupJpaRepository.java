package com.windmeal.store.repository;

import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionGroupJpaRepository extends JpaRepository<OptionGroup,Long> {
}
