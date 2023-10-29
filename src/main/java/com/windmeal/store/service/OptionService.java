package com.windmeal.store.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.dto.request.OptionCreateRequest;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.OptionGroupRepository;
import com.windmeal.store.repository.OptionSpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {

  private final OptionGroupRepository optionGroupRepository;
  private final OptionSpecificationRepository optionSpecificationRepository;
  private final MenuRepository menuRepository;

  @Transactional
  public void createOption(OptionCreateRequest request,Long menuId) {
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new MenuNotFoundException(
            ErrorCode.NOT_FOUND, "메뉴가 존재하지 않습니다."));
    OptionGroup savedOptionGroup = optionGroupRepository.save(request.toOptionGroupEntity(menu));
    optionSpecificationRepository.createOptionSpecs(request.getOptionSpec(),
        savedOptionGroup.getId());
  }
}
