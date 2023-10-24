package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.CategoryJpaRepository;
import com.windmeal.store.repository.CategoryRepository;
import com.windmeal.store.repository.StoreCategoryJpaRepository;
import com.windmeal.store.repository.StoreCategoryRepository;
import com.windmeal.store.repository.StoreJpaRepository;
import com.windmeal.store.validator.StoreValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

  private final StoreCategoryJpaRepository storeCategoryRepository;
  private final StoreJpaRepository storeJpaRepository;
  private final MemberRepository memberRepository;
  private final CategoryJpaRepository categoryRepository;
  private final StoreValidator storeValidator;

  @Transactional
  public StoreResponse createStore(StoreCreateRequest request, String imgUrl) {
    Member findMember = memberRepository.findById(request.getMemberId())
        .orElseThrow(); //Member Not Found 예외 추가 예정
    Store savedStore = storeJpaRepository.save(request.toEntity(findMember, imgUrl));
    categoryRepository.createCategories(
        request.getCategoryList());//category 에 존재하지 않는 경우 bulk 작업으로 저장
    List<Long> categoryIdList = categoryRepository.findAllByNameIn(request.getCategoryList())
        .stream()
        .map(Category::getId).collect(
            Collectors.toList());
    storeCategoryRepository.createStoreCategories(categoryIdList, savedStore.getId());

    return StoreResponse.of(savedStore, storeValidator);
  }

  @Transactional
  public String updateStorePhoto(Long storeId, String updateUrl) {
    Store findStore = storeJpaRepository.findById(storeId).orElseThrow(
        () -> new StoreNotFoundException(ErrorCode.NOT_FOUND, "매장이 존재하지 않습니다."));

    String originalPhoto = findStore.getPhoto();
    findStore.updatePhoto(updateUrl);

    return originalPhoto;
  }

  @Transactional
  public void updateStoreInfo(Long storeId, StoreUpdateRequest updateRequest) {
    Store findStore = storeJpaRepository.findById(storeId).orElseThrow(
        () -> new StoreNotFoundException(ErrorCode.NOT_FOUND, "매장이 존재하지 않습니다."));

    findStore.updateInfo(updateRequest);
  }
}
