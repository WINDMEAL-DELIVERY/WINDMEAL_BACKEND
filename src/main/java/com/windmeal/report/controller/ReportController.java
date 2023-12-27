package com.windmeal.report.controller;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.order.dto.request.DeliveryCancelRequest;
import com.windmeal.report.dto.request.ReportCreateRequest;
import com.windmeal.report.service.ReportService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "건의사항", description = "건의사항 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class ReportController {

  /**
   * 1. 건의 사항 생성
   * - 생성 시 관리자에게 알림 or 이메일 전송
   * 2. 건의 사항 조회
   * - 사용자 별로 검색 가능해야함
   */

  private final ReportService reportService;

  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "CREATED"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @PostMapping("/report")
  public ResultDataResponseDTO createReport(@RequestBody ReportCreateRequest request){
    Long currentMemberId = SecurityUtil.getCurrentMemberId();

    reportService.createReport(request.toServiceDto(currentMemberId));

    return ResultDataResponseDTO.empty(ErrorCode.CREATED);
  }

  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  @GetMapping("/report")
  public ResultDataResponseDTO getReportList(Pageable pageable,
      @Parameter(description = "닉네임 검색", required = false, schema = @Schema(example = "임동"))
      @RequestParam(required = false) String nickName,
      @Parameter(description = "이메일 검색", required = false, schema = @Schema(example = "idh1007@naver.com"))
      @RequestParam(required = false) String email){

    return ResultDataResponseDTO.of(reportService.getReportList(pageable,nickName,email));
  }
}

