package com.windmeal.store.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.request.OptionCreateRequest;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.repository.MenuJpaRepository;
import com.windmeal.store.repository.OptionGroupJpaRepository;
import com.windmeal.store.repository.OptionSpecificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {

  private final OptionGroupJpaRepository optionGroupRepository;
  private final OptionSpecificationJpaRepository optionSpecificationRepository;
  private final MenuJpaRepository menuRepository;
  @Transactional
  public void createOption(OptionCreateRequest request){
    Menu menu = menuRepository.findById(request.getMenuId())
        .orElseThrow(() -> new MenuNotFoundException(
            ErrorCode.NOT_FOUND, "메뉴가 존재하지 않습니다."));
    OptionGroup savedOptionGroup = optionGroupRepository.save(request.toOptionGroupEntity(menu));
    optionSpecificationRepository.createOptionSpecs(request.getOptionSpec(),
        savedOptionGroup.getId());
  }
}
