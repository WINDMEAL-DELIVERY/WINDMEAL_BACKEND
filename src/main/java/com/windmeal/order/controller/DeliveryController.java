package com.windmeal.order.controller;

import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.order.dto.request.DeliveryCancelRequest;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "배달 요청 카테고리", description = "배달 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class DeliveryController {

  private final DeliveryService deliveryService;

  @CacheEvict(value = "Orders", allEntries = true, cacheManager = "contentCacheManager") //데이터 삭제
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
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    deliveryService.createDelivery(request.toServiceDto(currentMemberId));
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


  @GetMapping("/delivery")
  @Operation(summary = "내가 배달중인 목록", description = "메인 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO getOwnDelivering(Pageable pageable){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();

    if(memberId==null) return ResultDataResponseDTO.empty();

    return ResultDataResponseDTO.of(deliveryService.getOwnDelivering(memberId,pageable));
  }

  @GetMapping("/delivery/order")
  @Operation(summary = "내가 요청한 목록", description = "메인 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO getOwnOrdering(Pageable pageable){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();

    if(memberId==null) return ResultDataResponseDTO.empty();

    return ResultDataResponseDTO.of(deliveryService.getOwnOrdering(memberId,pageable));
  }




  @GetMapping("/delivered")
  @Operation(summary = "내가 배달했던 목록 조회", description = "마이 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO getOwnDelivered(
      Pageable pageable,
      @Parameter(description = "날짜 필터링 시작", required = false, schema = @Schema(example = "2023-12-10"))
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @Parameter(description = "날짜 필터링 종료", required = false, schema = @Schema(example = "2023-12-10"))
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
      @Parameter(description = "카테고리 필터링", required = false, schema = @Schema(example = "커피"))
      @RequestParam(required = false) String storeCategory
  ){
    Long memberId = SecurityUtil.getCurrentMemberId();

    ;
    return ResultDataResponseDTO.of(deliveryService.getOwnDelivered(memberId,pageable,startDate,endDate,storeCategory));
  }


  @GetMapping("/delivered/price")
  @Operation(summary = "내가 배달했던 총 금액 조회", description = "마이 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO getOwnDeliveredTotalPrice(){
    Long memberId = SecurityUtil.getCurrentMemberId();

    return ResultDataResponseDTO.of(deliveryService.getOwnDeliveredTotalPrice(memberId));
  }
}
