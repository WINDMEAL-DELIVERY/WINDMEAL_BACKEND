package com.windmeal.store.repository;

import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OptionRepositoryImplTest extends IntegrationTestSupport {

  @Autowired
  private OptionSpecificationJpaRepository optionSpecificationRepository;
  @Autowired
  private OptionGroupJpaRepository optionGroupRepository;

  @AfterEach
  void tearDown() {
    optionSpecificationRepository.deleteAllInBatch();
    optionGroupRepository.deleteAllInBatch();
  }

  @DisplayName("옵션 그룹에 따른 옵션 스펙을 저장한다.")
  @Test
  void createOptionSpecs() {
    //given
    OptionGroup savedOptionGroup = optionGroupRepository.save(
        OptionGroup.builder().name("test").build());
    List<OptionSpecRequest> optionSpecs = new ArrayList<>();
    optionSpecs.add(buildOptionSpec("test1", 1000));
    optionSpecs.add(buildOptionSpec("test2", 2000));
    optionSpecs.add(buildOptionSpec("test3", 3000));

    //when
    Long optionGroupId = savedOptionGroup.getId();
    optionSpecificationRepository.createOptionSpecs(optionSpecs, optionGroupId);

    //then
    List<OptionSpecification> findOptionSpecs = optionSpecificationRepository.findByOptionGroupId(
        optionGroupId);
    assertThat(findOptionSpecs).hasSize(3)
        .extracting(OptionSpecification::getName)
        .containsExactlyInAnyOrder(
            "test1",
            "test2",
            "test3"

        );

  }

  private static OptionSpecRequest buildOptionSpec(String name, int price) {
    return OptionSpecRequest.builder().name(name).price(price).build();
  }
}