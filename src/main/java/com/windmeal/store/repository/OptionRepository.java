package com.windmeal.store.repository;

import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import java.util.List;

public interface OptionRepository {

  void createOptionSpecs(List<OptionSpecRequest> optionSpecifications,Long option_group_id);
}
