package com.windmeal.order.controller;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.order.dto.request.DeliveryCancelRequest;
import com.windmeal.order.dto.request.DeliveryCompleteRequest;
import com.windmeal.order.dto.request.DeliveryCreateRequest;
import com.windmeal.order.dto.response.DeliveryListResponse;
import com.windmeal.order.dto.response.OrderingListResponse;
import com.windmeal.order.dto.response.OwnDeliveryListResponse;
import com.windmeal.order.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
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
      @ApiResponse(responseCode = "201", description = "CREATED"),
      @ApiResponse(responseCode = "400", description = "이미 매칭된 주문",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  public ResultDataResponseDTO createDelivery(@RequestBody DeliveryCreateRequest request) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    deliveryService.createDelivery(request.toServiceDto(currentMemberId));

    return ResultDataResponseDTO.empty(ErrorCode.CREATED);
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
  public ResultDataResponseDTO cancelDelivery(@RequestBody DeliveryCancelRequest request) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    deliveryService.cancelDelivery(request.toServiceDto(currentMemberId));

    return ResultDataResponseDTO.empty();
  }


  @GetMapping("/delivery")
  @Operation(summary = "내가 배달중인 목록", description = "메인 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO<List<DeliveryListResponse>> getOwnDelivering(Pageable pageable){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();

    if(memberId==null) return ResultDataResponseDTO.empty();

    return ResultDataResponseDTO.of(deliveryService.getOwnDelivering(memberId,pageable));
  }

  @GetMapping("/delivery/order")
  @Operation(summary = "내가 요청한 목록", description = "메인 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO<List<OrderingListResponse>> getOwnOrdering(Pageable pageable){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();

    if(memberId==null) return ResultDataResponseDTO.empty();

    return ResultDataResponseDTO.of(deliveryService.getOwnOrdering(memberId,pageable));
  }




  @GetMapping("/delivered")
  @Operation(summary = "내가 배달했던 목록 조회", description = "마이 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  public ResultDataResponseDTO<Slice<OwnDeliveryListResponse>> getOwnDelivered(
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
  public ResultDataResponseDTO<Integer> getOwnDeliveredTotalPrice(){
    Long memberId = SecurityUtil.getCurrentMemberId();

    return ResultDataResponseDTO.of(deliveryService.getOwnDeliveredTotalPrice(memberId));
  }


  @PostMapping("/delivered")
  @Operation(summary = "배달 완료", description = "진행 중인 배달을 완료시킴")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "주문이 배달 중인 상태가 아님",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "배달원이 일치하지 않음",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 배달",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
  })
  public ResultDataResponseDTO completeDelivery(@RequestBody DeliveryCompleteRequest request) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    deliveryService.completeDelivery(request, currentMemberId);
    return ResultDataResponseDTO.empty();
  }
}
