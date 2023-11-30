package com.windmeal.store.service;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.place.Place;
import com.windmeal.model.place.PlaceRepository;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.StoreRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;

import java.time.LocalTime;

import static com.windmeal.Fixtures.aPlace;
import static org.assertj.core.api.Assertions.*;


class StoreServiceTest extends IntegrationTestSupport {

  @Autowired
  private StoreService storeService;
  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private PlaceRepository placeRepository;


  @AfterEach
  void tearDown() {
    storeRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
    placeRepository.deleteAllInBatch();
  }

  @DisplayName("매장을 생성한다.")
  @Test
  void createStore() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);

    //when
    StoreResponse response = storeService.createStore(request, "photo");

    //then
    assertThat(response)
        .extracting("ownerId", "name", "phoneNumber", "photo", "openTime", "closeTime")
        .containsExactly(savedMember.getId(), "testName", "010", "photo", LocalTime.of(10, 0, 0),
            LocalTime.of(22, 0, 0));
  }

  @DisplayName("매장의 사진을 변경하면 변경된 url 이 저장되어 있어야 한다.")
  @Test
  void updateStorePhoto() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);
    String originalPhoto = "photo";
    Place place = createPlace();
    Store savedStore = storeRepository.save(request.toEntity(savedMember, originalPhoto, place));

    //when
    String updateUrl = "update";
    Long storeId = savedStore.getId();
    String returnPhoto = storeService.updateStorePhoto(storeId, updateUrl);

    //then
    Store findStore = storeRepository.findById(savedStore.getId()).get();

    assertThat(findStore.getPhoto()).isEqualTo(updateUrl);
    assertThat(returnPhoto).isEqualTo(originalPhoto);
  }

  @DisplayName("매장의 사진을 변경할 때 매장이 존재하지 않는다면 예외가 발생한다.")
  @Test
  void updateStorePhotoWithNotFoundStore() {
    //given
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);
    Place place = createPlace();
    Store savedStore = storeRepository.save(request.toEntity(savedMember, "photo", place));

    //when
    String updateUrl = "update";
    Long storeId = 0L;

    //then
    assertThatThrownBy(() -> storeService.updateStorePhoto(storeId, updateUrl))
        .isInstanceOf(StoreNotFoundException.class)
        .hasMessage("매장이 존재하지 않습니다.");
  }

  @DisplayName("매장의 정보를 변경할 때 매장이 존재하지 않는다면 예외가 발생한다.")
  @Test
  void updateStoreInfoWithNotFoundStore() {
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);
    String originalPhoto = "photo";
    Place place = createPlace();
    Store savedStore = storeRepository.save(request.toEntity(savedMember, originalPhoto, place));

    //when
    Long storeId = 0L;
    StoreUpdateRequest updateRequest = StoreUpdateRequest.builder().build();

    //then
    assertThatThrownBy(() -> storeService.updateStoreInfo(storeId, updateRequest))
        .isInstanceOf(StoreNotFoundException.class)
        .hasMessage("매장이 존재하지 않습니다.");
  }

  @DisplayName("매장의 정보를 변경하면 반영되어야한다.")
  @Test
  void updateStoreInfo() {
    Member savedMember = createMember("test");
    StoreCreateRequest request = createStoreCreateRequest(
        savedMember, "010", "testName"
        , LocalTime.of(10, 0, 0), LocalTime.of(22, 0, 0)
        , 1234.567, 567.812);
    String originalPhoto = "photo";
    Place place = createPlace();
    Store savedStore = storeRepository.save(request.toEntity(savedMember, originalPhoto, place));

    //when
    Long storeId = savedStore.getId();
    StoreUpdateRequest updateRequest = StoreUpdateRequest.builder()
        .placeName("place")
        .latitude(1.234)
        .longitude(34.543543)
        .closeTime(LocalTime.of(10, 1, 1))
        .openTime(LocalTime.of(07, 1, 1))
        .name("changeName")
        .phoneNumber("changePhone")
        .build();
    storeService.updateStoreInfo(storeId, updateRequest);

    //then
    Store store = storeRepository.findById(savedStore.getId()).get();
    assertThat(store)
        .extracting(s -> s.getName(), s -> s.getPhoneNumber(),
            s -> s.getOpenTime(), s -> s.getCloseTime())
        .containsExactly(updateRequest.getName(), updateRequest.getPhoneNumber(),
            updateRequest.getOpenTime(), updateRequest.getCloseTime()
            );

  }

  private Place createPlace() {
    return placeRepository.save(aPlace().build());
  }

  private static StoreCreateRequest createStoreCreateRequest(
      Member savedMember, String phoneNumber, String name, LocalTime openTime, LocalTime closeTime,
      Double lat, Double lon) {
    return StoreCreateRequest.builder()
        .memberId(savedMember.getId())
        .name(name)
        .phoneNumber(phoneNumber)
        .memberId(savedMember.getId())
        .openTime(openTime)
        .closeTime(closeTime)
        .latitude(lat)
        .longitude(lon)
        .categoryList(new ArrayList<>())
        .build();
  }

  private Member createMember(String name) {
    return memberRepository.save(Member.builder()
        .name(name).build());
  }


}