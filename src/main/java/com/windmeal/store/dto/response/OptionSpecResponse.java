package com.windmeal.store.dto.response;

import com.windmeal.generic.domain.Money;
import com.windmeal.store.domain.OptionSpecification;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OptionSpecResponse {

  private Long optionGroupId;
  private Long optionSpecId;
  private String name;
  private Money price;


}
