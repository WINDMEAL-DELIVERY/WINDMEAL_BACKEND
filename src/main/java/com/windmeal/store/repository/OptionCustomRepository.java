package com.windmeal.store.repository;

import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import com.windmeal.store.dto.response.OptionSpecResponse;
import java.util.List;

public interface OptionCustomRepository {

  void createOptionSpecs(List<OptionSpecRequest> optionSpecifications, Long option_group_id);
  List<OptionSpecResponse> findByOptionGroupIdIn(List<Long> optionGroupIdList);
}
