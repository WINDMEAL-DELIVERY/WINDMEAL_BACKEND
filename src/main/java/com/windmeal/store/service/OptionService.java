package com.windmeal.store.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.dto.request.OptionCreateRequest;
import com.windmeal.store.dto.response.MenuOptionResponse;
import com.windmeal.store.dto.response.OptionSpecResponse;
import com.windmeal.store.exception.MenuNotFoundException;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.OptionGroupRepository;
import com.windmeal.store.repository.OptionSpecificationRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
        .orElseThrow(() -> new MenuNotFoundException());
    OptionGroup savedOptionGroup = optionGroupRepository.save(request.toOptionGroupEntity(menu));
    optionSpecificationRepository.createOptionSpecs(request.getOptionSpecs(),
        savedOptionGroup.getId());
  }

  @Cacheable(value = "Menu", key = "#menuId", cacheManager = "contentCacheManager")
  public MenuOptionResponse getMenuGroups(Long menuId) {
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new MenuNotFoundException());
    List<OptionGroup> optionGroups = optionGroupRepository.findByMenuId(menu.getId());
    List<Long> optionGroupIdList = optionGroups.stream().map(OptionGroup::getId).collect(Collectors.toList());
    Map<Long, List<OptionSpecResponse>> optionSpecs = optionSpecificationRepository.findByOptionGroupIdIn(
            optionGroupIdList).stream()
        .collect(Collectors.groupingBy(OptionSpecResponse::getOptionGroupId));
    return new MenuOptionResponse(menu,optionGroups,optionSpecs);
  }
}
