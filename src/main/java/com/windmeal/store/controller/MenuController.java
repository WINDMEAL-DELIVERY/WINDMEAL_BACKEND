package com.windmeal.store.controller;

import com.windmeal.global.S3.S3Util;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.MenuCreateRequest;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "메뉴", description = "메뉴 관련 api 입니다.")
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
  @Operation(summary = "메뉴 생성 요청", description = "메뉴가 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PostMapping("/menu")
  public ResultDataResponseDTO createMenu(
      @Valid @RequestPart MenuCreateRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file) {

    String imgUrl = s3Util.imgUpload(file);
    menuService.createMenu(request, imgUrl);
    return ResultDataResponseDTO.empty();
  }
}
