package com.windmeal.order.controller;

import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.order.dto.OrderCreateRequest;
import com.windmeal.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문 카테고리", description = "주문 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "가게 카테고리 생성 요청", description = "가게 카테고리가 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @PostMapping("/order")
  public void createOrder(@RequestBody OrderCreateRequest request){
    orderService.createOrder(request);
  }

}
