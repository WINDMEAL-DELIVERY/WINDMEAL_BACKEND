package com.windmeal.store.controller;

import com.windmeal.global.S3.S3Util;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.service.MenuService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 메뉴 생성
 * 메뉴 정보 조회
 * TODO 메뉴 삭제
 * TODO 메뉴 수정
 */
@RestController
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;
  private final S3Util s3Util;

  /**
   * 메뉴 생성
   * @param request
   * @param file
   * @return
   */
  @PostMapping("/menu")
  public ResultDataResponseDTO createMenu(
      @Valid @RequestPart MenuCreateRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file) {

    String imgUrl = s3Util.imgUpload(file);
    menuService.createMenu(request, imgUrl);
    return ResultDataResponseDTO.empty();
  }
}
