package com.windmeal.order.controller;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.order.dto.request.DeliveryFeeUpdateRequest;
import com.windmeal.order.dto.request.OrderCreateRequest;
import com.windmeal.order.dto.request.OrderDeleteRequest;
import com.windmeal.order.dto.response.OrderDetailResponse;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.order.dto.response.OwnOrderListResponse;
import com.windmeal.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@Tag(name = "주문 카테고리", description = "주문 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "주문 요청 생성 요청", description = "주문 요청이 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "CREATED"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @PostMapping("/order")
  public ResultDataResponseDTO createOrder(@RequestBody OrderCreateRequest request){
    Long memberId = SecurityUtil.getCurrentMemberId();
    orderService.createOrder(request.toServiceDto(memberId));

    return ResultDataResponseDTO.empty(ErrorCode.CREATED);
  }

  @Operation(summary = "주문 요청 삭제 요청", description = "주문 요청이 삭제됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @DeleteMapping("/order")
  public ResultDataResponseDTO deleteOrder(@RequestBody OrderDeleteRequest request){
    orderService.deleteOrder(request);
    return ResultDataResponseDTO.empty();
  }

  @Operation(summary = "주문 내역 조회", description = "주문 내역 리스트를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  /**
   * 주문 정보 조회
   * 필터링 조건
   * - 식당
   * - 도착지
   * - 도착 시간
   * - 음식 종류
   * @return
   */
  @GetMapping("/order")
  public ResultDataResponseDTO<Slice<OrderListResponse>> getAllOrder(
      Pageable pageable,
      @Parameter(description = "가게 id", required = false, schema = @Schema(example = "1"))
      @RequestParam(required = false) Long storeId,
      @Parameter(description = "도착지 id", required = false, schema = @Schema(example = "1"))
      @RequestParam(required = false) Long placeId,
      @Parameter(description = "도착 예정 시간", required = false, schema = @Schema(example = "23:10:00"))
      @RequestParam(required = false) String eta,
      @Parameter(description = "검색 키워드", required = false, schema = @Schema(example = "카페"))
      @RequestParam(required = false) String storeCategory
  ){
    Long memberId = SecurityUtil.getCurrentNullableMemberId();
    RestSlice<OrderListResponse> orders = orderService.getAllOrder(pageable, storeId, eta,
        storeCategory, placeId,memberId);

    return ResultDataResponseDTO.of(orders);
  }


  @Operation(summary = "주문 내역 상세 조회", description = "주문 내역의 상세 내용을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @GetMapping("/order/{orderId}")
  public ResultDataResponseDTO<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId){
    return ResultDataResponseDTO.of(orderService.getOrderDetail(orderId));
  }



  @Operation(summary = "내가 주문했던 목록 조회", description = "마이 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  @GetMapping("/ordered")
  public ResultDataResponseDTO<Slice<OwnOrderListResponse>> getOwnOrdered(
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
    return ResultDataResponseDTO.of(orderService.getOwnOrdered(memberId,pageable,startDate,endDate,storeCategory));
  }


  @Operation(summary = "내가 주문 시 이득 본 총 금액 조회", description = "마이 페이지")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  @GetMapping("/ordered/price")
  public ResultDataResponseDTO<Integer> getOwnOrderedTotalPrice(){
    Long memberId = SecurityUtil.getCurrentMemberId();


    return ResultDataResponseDTO.of(orderService.getOwnOrderedTotalPrice(memberId));
  }

  @Operation(summary = "배달료 수정", description = "내 주문에서 배달료를 수정함")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @PatchMapping("/ordered/price")
  public ResultDataResponseDTO getOwnOrderedTotalPrice(@RequestBody DeliveryFeeUpdateRequest request){
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    orderService.updateDeliveryFee(request, currentMemberId);
    return ResultDataResponseDTO.empty();
  }

}
