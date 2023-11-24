package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.dto.response.StoreMenuResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.CategoryRepository;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.StoreCategoryRepository;
import com.windmeal.store.repository.StoreRepository;
import com.windmeal.store.validator.StoreValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

  private final StoreCategoryRepository storeCategoryRepository;
  private final StoreRepository storeRepository;
  private final MemberRepository memberRepository;
  private final MenuCategoryRepository menuCategoryRepository;
  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;
  private final StoreValidator storeValidator;

  @Transactional
  public StoreResponse createStore(StoreCreateRequest request, String imgUrl) {
    Member findMember = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND,"사용자가 존재하지 않습니다.")); //Member Not Found 예외 추가 예정
    Store savedStore = storeRepository.save(request.toEntity(findMember, imgUrl));
    if(!request.getCategoryList().isEmpty()) {
      categoryRepository.createCategories(
          request.getCategoryList());//category 에 존재하지 않는 경우 bulk 작업으로 저장
      List<Long> categoryIdList = categoryRepository.findAllByNameIn(request.getCategoryList())
          .stream()
          .map(Category::getId).collect(
              Collectors.toList());
      storeCategoryRepository.createStoreCategories(categoryIdList, savedStore.getId());
    }
    return StoreResponse.of(savedStore);
  }

  @Transactional
  public String updateStorePhoto(Long storeId, String updateUrl) {
    Store findStore = storeRepository.findById(storeId).orElseThrow(
        () -> new StoreNotFoundException(ErrorCode.NOT_FOUND, "매장이 존재하지 않습니다."));

    String originalPhoto = findStore.getPhoto();
    findStore.updatePhoto(updateUrl);

    return originalPhoto;
  }

  @Transactional
  public void updateStoreInfo(Long storeId, StoreUpdateRequest updateRequest) {
    Store findStore = storeRepository.findById(storeId).orElseThrow(
        () -> new StoreNotFoundException(ErrorCode.NOT_FOUND, "매장이 존재하지 않습니다."));

    findStore.updateInfo(updateRequest);
  }

  public StoreMenuResponse getStoreInfo(Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new StoreNotFoundException(ErrorCode.NOT_FOUND, "매장이 존재하지 않습니다."));
    List<MenuCategory> menuCategories = menuCategoryRepository.findByStoreId(store.getId());
    List<Long> menuCategoryIds = menuCategories.stream().map(MenuCategory::getId)
        .collect(Collectors.toList());
    List<MenuResponse> menus = menuRepository.findByMenuCategoryIdIn(menuCategoryIds);
    return new StoreMenuResponse(store, menuCategories, menus);
  }
}
