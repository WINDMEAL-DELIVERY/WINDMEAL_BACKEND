package com.windmeal.store.repository;

import static org.assertj.core.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import com.windmeal.store.dto.response.OptionSpecResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OptionCustomRepositoryImplTest extends IntegrationTestSupport {

  @Autowired
  private OptionSpecificationRepository optionSpecificationRepository;
  @Autowired
  private OptionGroupRepository optionGroupRepository;

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


  @DisplayName("옵션 그룹 아이디에 포함되는 옵션 상세 리스트를 가져온다.")
  @Test
  void findByOptionGroupIdIn() {
    //given
    OptionGroup savedOptionGroup1 = optionGroupRepository.save(
        OptionGroup.builder().name("test1").build());
    List<OptionSpecRequest> optionSpecs1 = new ArrayList<>();
    optionSpecs1.add(buildOptionSpec("test1", 1000));
    optionSpecs1.add(buildOptionSpec("test2", 2000));
    optionSpecs1.add(buildOptionSpec("test3", 3000));

    OptionGroup savedOptionGroup2 = optionGroupRepository.save(
        OptionGroup.builder().name("test2").build());
    List<OptionSpecRequest> optionSpecs2 = new ArrayList<>();
    optionSpecs2.add(buildOptionSpec("test4", 1000));
    optionSpecs2.add(buildOptionSpec("test5", 2000));
    optionSpecs2.add(buildOptionSpec("test6", 3000));

    OptionGroup savedOptionGroup3 = optionGroupRepository.save(
        OptionGroup.builder().name("test3").build());
    List<OptionSpecRequest> optionSpecs3 = new ArrayList<>();
    optionSpecs3.add(buildOptionSpec("test7", 1000));
    optionSpecs3.add(buildOptionSpec("test8", 2000));

    Long optionGroupId1 = savedOptionGroup1.getId();
    optionSpecificationRepository.createOptionSpecs(optionSpecs1, optionGroupId1);
    Long optionGroupId2 = savedOptionGroup2.getId();
    optionSpecificationRepository.createOptionSpecs(optionSpecs2, optionGroupId2);
    Long optionGroupId3 = savedOptionGroup3.getId();
    optionSpecificationRepository.createOptionSpecs(optionSpecs3, optionGroupId3);

    //when
    List<OptionSpecResponse> optionSpecs = optionSpecificationRepository.findByOptionGroupIdIn(
        Arrays.asList(optionGroupId1, optionGroupId2));

    //then
    assertThat(optionSpecs)
        .hasSize(6)
        .extracting(OptionSpecResponse::getOptionGroupId, OptionSpecResponse::getName,
            OptionSpecResponse -> OptionSpecResponse.getPrice().wons())
        .containsExactlyInAnyOrder(
            tuple(optionGroupId1,"test1", 1000),
            tuple(optionGroupId1,"test2", 2000),
            tuple(optionGroupId1,"test3", 3000),
            tuple(optionGroupId2,"test4", 1000),
            tuple(optionGroupId2,"test5", 2000),
            tuple(optionGroupId2,"test6", 3000)
        );
  }


  private static OptionSpecRequest buildOptionSpec(String name, int price) {
    return OptionSpecRequest.builder().name(name).price(price).build();
  }
}