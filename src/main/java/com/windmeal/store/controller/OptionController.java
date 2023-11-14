package com.windmeal.store.controller;


import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.OptionCreateRequest;
import com.windmeal.store.dto.response.MenuOptionResponse;
import com.windmeal.store.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "옵션", description = "옵션 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class OptionController {

  private final OptionService optionService;

  @Operation(summary = "옵션 생성 요청", description = "옵션이 생성됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @PostMapping("/menu/{menuId}/option")
  public ResultDataResponseDTO createOption(
      @Valid @RequestBody OptionCreateRequest request,
      @PathVariable Long menuId){
    optionService.createOption(request,menuId);
    return ResultDataResponseDTO.empty();
  }
  @Operation(summary = "메뉴의 옵션 조회 요청", description = "메뉴의 옵션이 조회됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))})
  @GetMapping("/menu/{menuId}/option")
  public ResultDataResponseDTO<MenuOptionResponse> getMenuGroups(@PathVariable Long menuId){

    return ResultDataResponseDTO.of(optionService.getMenuGroups(menuId));
  }
}
