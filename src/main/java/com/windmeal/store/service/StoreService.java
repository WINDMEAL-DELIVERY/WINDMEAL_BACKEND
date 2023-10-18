package com.windmeal.store.service;


import com.windmeal.global.S3.S3Util;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.domain.Category;
import com.windmeal.store.domain.Store;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.dto.response.CategoryResponse;
import com.windmeal.store.exception.StoreNotFoundException;
import com.windmeal.store.repository.StoreRepository;
import com.windmeal.store.validator.StoreValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;
    private final StoreValidator storeValidator;
    @Transactional
    public StoreResponse createStore(StoreCreateRequest request, String imgUrl){
        Member findMember = memberRepository.findById(request.getMemberId()).orElseThrow(); //Member Not Found 예외 추가 예정
        Store savedStore = storeRepository.save(request.toEntity(findMember,imgUrl));

        return StoreResponse.of(savedStore,storeValidator);
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
}
