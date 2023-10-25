package com.windmeal.store.controller;

import com.windmeal.global.S3.S3Util;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;
  private final S3Util s3Util;

  /**
   * 가게 생성
   *
   * @param request
   * @param file
   * @return
   */
  @PostMapping("/store")
  public ResultDataResponseDTO createStore(
      @Valid @RequestPart StoreCreateRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file) {

    String imgUrl = s3Util.imgUpload(file);
    StoreResponse response = storeService.createStore(request, imgUrl);
    return ResultDataResponseDTO.of(response);
  }

  /**
   * 가게 사진 수정
   *
   * @param file
   * @param storeId
   * @return
   */
  @PatchMapping("/store/{storeId}/photo")
  public ResultDataResponseDTO updateStorePhoto(
      @RequestPart("file") MultipartFile file, @PathVariable Long storeId) {

    String imgUrl = s3Util.imgUpload(file);
    String originalPhoto = storeService.updateStorePhoto(storeId, imgUrl);
    s3Util.delete(originalPhoto);
    return ResultDataResponseDTO.empty();
  }

  /**
   * 가게 정보 수정
   *
   * @param storeId
   * @param updateRequest
   * @return
   */
  @PatchMapping("/store/{storeId}/info")
  public ResultDataResponseDTO updateStoreInfo(
      @PathVariable Long storeId, @RequestBody StoreUpdateRequest updateRequest) {

    storeService.updateStoreInfo(storeId, updateRequest);
    return ResultDataResponseDTO.empty();
  }

  @GetMapping("/store/{storeId}")
  public ResultDataResponseDTO getStoreInfo(@PathVariable Long storeId) {
    return ResultDataResponseDTO.of(storeService.getStoreInfo(storeId));

  }
}
