package com.windmeal.store.repository;

import com.windmeal.store.domain.OptionSpecification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OptionSpecificationRepository extends JpaRepository<OptionSpecification, Long>,
    OptionCustomRepository {

  List<OptionSpecification> findByOptionGroupId(Long optionGroupId);
}
