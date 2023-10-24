package com.windmeal.store.repository;

import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionSpecificationJpaRepository extends JpaRepository<OptionSpecification,Long> {
}
