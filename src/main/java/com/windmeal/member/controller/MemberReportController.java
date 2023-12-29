package com.windmeal.member.controller;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.dto.request.MemberReportCreateRequest;
import com.windmeal.member.dto.response.MemberReportListResponse;
import com.windmeal.member.service.MemberReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequiredArgsConstructor
@RequestMapping(value = "/api/member")
@Tag(name = "멤버 신고", description = "멤버 신고 관련 api 입니다.")
public class MemberReportController {

  private final MemberReportService memberReportService;

  @Operation(summary = "멤버 신고 추가", description = "멤버 신고 기능입니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "닉네임 설정 성공"),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
  })
  @PostMapping("/report")
  public ResultDataResponseDTO createMemberReport(MemberReportCreateRequest request){
    Long currentMemberId = SecurityUtil.getCurrentMemberId();

    memberReportService.createMemberReport(request.toServiceDto(currentMemberId));
    return ResultDataResponseDTO.empty(ErrorCode.CREATED);
  }

  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK")
  })
  @GetMapping("/report")
  public ResultDataResponseDTO<Slice<MemberReportListResponse>> getReportList(Pageable pageable,
      @Parameter(description = "검색의 대상(신고한 사람을 검색하는지 신고 당한 사람을 검색하는지)", required = false, schema = @Schema(example = "임동"))
      @RequestParam(required = false) Boolean isReported,
      @Parameter(description = "닉네임 검색", required = false, schema = @Schema(example = "임동"))
      @RequestParam(required = false) String nickName,
      @Parameter(description = "이메일 검색", required = false, schema = @Schema(example = "idh1007@naver.com"))
      @RequestParam(required = false) String email){

    return ResultDataResponseDTO.of(memberReportService.getReportList(pageable,nickName,email,isReported));
  }
}
