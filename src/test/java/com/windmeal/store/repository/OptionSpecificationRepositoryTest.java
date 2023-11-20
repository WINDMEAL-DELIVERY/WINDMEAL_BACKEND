package com.windmeal.store.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.OptionSpecification;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OptionSpecificationRepositoryTest extends IntegrationTestSupport {

  @Autowired
  OptionSpecificationRepository optionSpecificationRepository;


  @DisplayName("ID 리스트에 해당되는 OptionSpecification 의 총 가격을 가져온다.")
  @Test
  void calculateOptionSpecTotalPrice() {
    //given
    OptionSpecification optionSpecification1 = optionSpecificationRepository.save(
        OptionSpecification.builder().price(Money.wons(1000)).build());
    OptionSpecification optionSpecification2 = optionSpecificationRepository.save(
        OptionSpecification.builder().price(Money.wons(3000)).build());
    OptionSpecification optionSpecification3 = optionSpecificationRepository.save(
        OptionSpecification.builder().price(Money.wons(4000)).build());
    //when
    Long totalPrice = optionSpecificationRepository.calculateOptionSpecTotalPrice(
        Arrays.asList(optionSpecification1.getId(), optionSpecification2.getId(),
            optionSpecification3.getId()));
    //then
    Assertions.assertThat(totalPrice).isEqualTo(8000);
  }
}