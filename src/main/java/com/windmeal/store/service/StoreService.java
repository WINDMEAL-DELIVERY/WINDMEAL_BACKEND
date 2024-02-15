package com.windmeal.store.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.Place;
import com.windmeal.model.place.PlaceRepository;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.MenuCategory;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.StoreCategory;
import com.windmeal.store.dto.request.CategoryCreateRequest;
import com.windmeal.store.dto.request.StoreCategoryCreateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.AllStoreResponse;
import com.windmeal.store.dto.response.CategoryStoreMenuResponse;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.dto.response.StoreCategoryResponse;
import com.windmeal.store.dto.response.StoreMenuResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.StoreCategoryNotFoundException;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.CategoryRepository;
import com.windmeal.store.repository.MenuCategoryRepository;
import com.windmeal.store.repository.MenuRepository;
import com.windmeal.store.repository.StoreCategoryRepository;
import com.windmeal.store.repository.StoreRepository;
import com.windmeal.store.validator.StoreValidator;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
  private final PlaceRepository placeRepository;

  @Transactional
  public StoreResponse createStore(StoreCreateRequest request, String imgUrl) {
    Member findMember = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException()); //Member Not Found 예외 추가 예정
    Place place = placeRepository.findByNameAndLongitudeAndLatitude(request.getPlaceName(),request.getLongitude(),request.getLatitude())
        .orElseGet(() -> placeRepository.save(request.toPlaceEntity()));

    Store savedStore = storeRepository.save(request.toEntity(findMember, imgUrl,place));
    if(!request.getCategoryList().isEmpty()) {
      categoryRepository.createCategories(
          request.getCategoryList());//category 에 존재하지 않는 경우 bulk 작업으로 저장
      List<Long> categoryIdList = categoryRepository.findAllByNameIn(request.getCategoryList())
          .stream()
          .map(Category::getId).collect(
              Collectors.toList());
      storeCategoryRepository.createStoreCategories(categoryIdList, savedStore.getId());
    }
    return StoreResponse.of(savedStore,place);
  }

  @Transactional
  public String updateStorePhoto(Long storeId, String updateUrl) {
    Store findStore = storeRepository.findById(storeId).orElseThrow(
        () -> new StoreNotFoundException());

    String originalPhoto = findStore.getPhoto();
    findStore.updatePhoto(updateUrl);

    return originalPhoto;
  }

  @Transactional
  public void updateStoreInfo(Long storeId, StoreUpdateRequest request) {
    Store findStore = storeRepository.findById(storeId).orElseThrow(
        () -> new StoreNotFoundException());
    Place place = placeRepository.findByNameAndLongitudeAndLatitude(request.getPlaceName(),request.getLongitude(),request.getLatitude())
        .orElseGet(() -> placeRepository.save(request.toPlaceEntity()));

    findStore.updateInfo(request,place);
  }
  @Cacheable(value = "Store", key = "#storeId", cacheManager = "contentCacheManager")
  public StoreMenuResponse getStoreInfo(Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new StoreNotFoundException());
    List<MenuCategory> menuCategories = menuCategoryRepository.findByStoreId(store.getId());
    List<Long> menuCategoryIds = menuCategories.stream().map(MenuCategory::getId)
        .collect(Collectors.toList());
    List<MenuResponse> menus = menuRepository.findByMenuCategoryIdIn(menuCategoryIds);
    return new StoreMenuResponse(store, menuCategories, menus,store.getPlace());
  }

  public Slice<AllStoreResponse> getAllStoreInfo(Pageable pageable){
    return storeRepository.getAllStoreInfo(pageable);
  }

  @Cacheable(value = "Orders", key = "0",cacheManager = "contentCacheManager",
      condition = "#storeId == null&&#eta==null&&#storeCategory==null&&#placeId==null&&#orderStatus==null&&#isOpen==false")
  public List<OrderMapListResponse> getAllStoresForMap(Long storeId, String eta, String storeCategory, Long placeId, OrderStatus orderStatus, Boolean isOpen) {
      return storeRepository.getStoreMapList(storeId, eta, storeCategory, placeId, orderStatus, isOpen);
  }

  public CategoryStoreMenuResponse getStoreInfoForCms(Long storeId) {
    StoreMenuResponse storeInfo = getStoreInfo(storeId);

    List<StoreCategoryResponse> storeCategories = storeCategoryRepository.getStoreCategories(
        storeId);
    return CategoryStoreMenuResponse.of(storeCategories,storeInfo);
  }

  @Transactional
  public void deleteStoreCategory(Long storeCategoryId) {
    StoreCategory storeCategory = storeCategoryRepository.findById(storeCategoryId)
        .orElseThrow(() -> new StoreCategoryNotFoundException());

    storeCategoryRepository.delete(storeCategory);
  }

  @Transactional
  public void createStoreCategory(StoreCategoryCreateRequest request) {

    categoryRepository.createCategories(
        Collections.singletonList(request.getName()));

    List<Long> categoryIdList = categoryRepository.findAllByNameIn(
            Collections.singletonList(request.getName()))
        .stream()
        .map(Category::getId).collect(
            Collectors.toList());
    storeCategoryRepository.createStoreCategoriesNotExist(categoryIdList, request.getStoreId());

  }
}
