package com.windmeal.store.repository;

import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import java.util.List;

public interface OptionCustomRepository {

  void createOptionSpecs(List<OptionSpecRequest> optionSpecifications, Long option_group_id);
}
