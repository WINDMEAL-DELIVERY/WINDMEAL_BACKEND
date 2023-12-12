package com.windmeal.store.controller;

import com.windmeal.global.S3.S3Util;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.StoreCategoryCreateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.request.StoreUpdateRequest;
import com.windmeal.store.dto.response.AllStoreResponse;
import com.windmeal.store.dto.response.CategoryStoreMenuResponse;
import com.windmeal.store.dto.response.StoreMenuResponse;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 가게 생성 가게 정보 수정(이미지 제외) 가게 이미지 수정 가게 삭제 가게 정보 조회
 */
@Tag(name = "가게", description = "가게 관련 api 입니다.")
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
  @Operation(summary = "가게 생성", description = "가게가 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "CREATED",
          content = @Content(schema = @Schema(implementation = StoreResponse.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PostMapping("/store")
  public ResultDataResponseDTO<StoreResponse> createStore(
      @RequestPart("request") @Parameter(description = "가게 생성 요청 데이터", required = true, schema = @Schema(type = "application/json",implementation = StoreCreateRequest.class))
      @Valid StoreCreateRequest request,
      @RequestPart(value = "file", required = false) @Parameter(description = "이미지 파일", schema = @Schema(type = "image/png"))
      MultipartFile file) {
    String imgUrl = s3Util.imgUpload(file);
    StoreResponse response = storeService.createStore(request, imgUrl);
    return ResultDataResponseDTO.of(response,ErrorCode.CREATED);
  }

  /**
   * 가게 사진 수정
   *
   * @param file
   * @param storeId
   * @return
   */
  @Operation(summary = "가게 사진 수정", description = "가게 사진이 수정됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
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
  @Operation(summary = "가게 정보 수정", description = "가게 정보가 수정됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PatchMapping("/store/{storeId}/info")
  public ResultDataResponseDTO updateStoreInfo(
      @PathVariable Long storeId, @RequestBody StoreUpdateRequest updateRequest) {

    storeService.updateStoreInfo(storeId, updateRequest);
    return ResultDataResponseDTO.empty();
  }


  /**
   * 가게 정보 조회
   *
   * @param storeId
   * @return
   */
  @Operation(summary = "가게 정보 조회", description = "가게 정보가 조회됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @GetMapping("/store/{storeId}")
  public ResultDataResponseDTO<StoreMenuResponse> getStoreInfo(@PathVariable Long storeId) {
    return ResultDataResponseDTO.of(storeService.getStoreInfo(storeId));

  }

  @Operation(summary = "모든 가게 정보 조회", description = "모든 가게의 정보가 조회됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @GetMapping("/store")
  public ResultDataResponseDTO<Slice<AllStoreResponse>> getAllStoreInfo(Pageable pageable){
    return ResultDataResponseDTO.of(storeService.getAllStoreInfo(pageable));
  }




  @Operation(summary = "가게 정보 조회(CMS)", description = "가게 정보가 조회됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @GetMapping("/cms/store/{storeId}")
  public ResultDataResponseDTO<CategoryStoreMenuResponse> getStoreInfoForCms(@PathVariable Long storeId) {
    return ResultDataResponseDTO.of(storeService.getStoreInfoForCms(storeId));
  }

  @Operation(summary = "가게 카테고리 삭제", description = "가게 카테고리가 삭제됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @DeleteMapping("/cms/storeCategory/{storeCategoryId}")
  public ResultDataResponseDTO deleteStoreCategory(@PathVariable Long storeCategoryId) {

    storeService.deleteStoreCategory(storeCategoryId);

    return ResultDataResponseDTO.empty();
  }

  @Operation(summary = "가게 카테고리 생성", description = "가게 카테고리가 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "CREATED"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PostMapping("/cms/store/{storeId}/storeCategory")
  public ResultDataResponseDTO createStoreCategory(
      @PathVariable Long storeId, @RequestBody StoreCategoryCreateRequest request) {

    storeService.createStoreCategory(request.toServiceDto(storeId));

    return ResultDataResponseDTO.empty(ErrorCode.CREATED);
  }
}
