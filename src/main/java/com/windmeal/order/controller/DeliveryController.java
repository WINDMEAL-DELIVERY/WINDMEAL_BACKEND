package com.windmeal.order.controller;

import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.order.dto.request.DeliveryCancelRequest;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "배달 요청 카테고리", description = "배달 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class DeliveryController {

  private final DeliveryService deliveryService;

  @PostMapping("/delivery")
  @Operation(summary = "배달 요청 생성 요청", description = "배달 요청이 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "이미 매칭된 주문",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  public void createDelivery(@RequestBody DeliveryCreateRequest request) {
    deliveryService.createDelivery(request);
  }


  @PatchMapping("/delivery")
  @Operation(summary = "배달 취소 요청", description = "배달 취소 요청이 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "이미 매칭된 주문",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  public void cancelDelivery(@RequestBody DeliveryCancelRequest request) {
    deliveryService.cancelDelivery(request);
  }

  //TODO 내가 배달중인 목록

  @GetMapping("/delivery")
  public ResultDataResponseDTO getOwnDelivering(Pageable pageable){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();

    if(memberId==null) return ResultDataResponseDTO.empty();

    return ResultDataResponseDTO.of(deliveryService.getOwnDelivering(memberId,pageable));
  }

  //TODO 내가 요청한 목록
  @GetMapping("/delivery/order")
  public ResultDataResponseDTO getOwnOrdering(Pageable pageable){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();

    if(memberId==null) return ResultDataResponseDTO.empty();

    return ResultDataResponseDTO.of(deliveryService.getOwnOrdering(memberId,pageable));
  }

}