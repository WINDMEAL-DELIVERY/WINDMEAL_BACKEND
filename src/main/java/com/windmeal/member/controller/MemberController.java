package com.windmeal.member.controller;

import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.dto.request.MemberInfoRequest;
import com.windmeal.member.dto.request.NicknameRequest;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/member")
@Tag(name = "멤버", description = "멤버(사용자) 관련 api 입니다.")
public class MemberController {

  private final MemberService memberService;

  @Operation(summary = "닉네임 등록 요청", description = "최초 회원가입 시, 닉네임을 추가로 입력 받습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "닉네임 설정 성공"),
      @ApiResponse(responseCode = "400", description = "이미 사용 중인 닉네임입니다."),
      @ApiResponse(responseCode = "400", description = "닉네임에는 특수문자가 포함될 수 없습니다."),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
  })
  @PostMapping(value = "/nickname")
  public ResultDataResponseDTO<String> registerNickname(
      @Valid @RequestBody NicknameRequest request) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    String nickname = memberService.registerNickname(request, currentMemberId);
    return ResultDataResponseDTO.of(nickname);
  }

  @Operation(summary = "닉네임 중복 확인 요청", description = "닉네임이 존재하는지 확인합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "닉네임 확인"),
  })
  @GetMapping(value = "/nickname/{nickname}/exists")
  public ResultDataResponseDTO<Boolean> checkNickname(@PathVariable String nickname) {
    Boolean result = memberService.checkNickname(nickname);
    return ResultDataResponseDTO.of(result);
  }

  @Operation(summary = "알람 토큰과 사용자 정보 교환", description = "로그인 후 리다이렉트 하기 전 호출될 api")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공적으로 교환 완료"),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
      @ApiResponse(responseCode = "600", description = "암호화 실패"),
  })
  @PostMapping(value = "/info")
  public ResultDataResponseDTO<MemberInfoDTO> tokenExchange(
      @RequestBody MemberInfoRequest memberInfoRequest) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    MemberInfoDTO memberInfo = memberService.memberInfoDetails(memberInfoRequest, currentMemberId);
    return ResultDataResponseDTO.of(memberInfo);
  }

  @GetMapping
  public ResultDataResponseDTO<MemberInfoDTO> myInfo() {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    return ResultDataResponseDTO.of(memberService.myInfoDetails(currentMemberId));
  }

  @Operation(summary = "알람 테스트용 API", description = "프론트엔드 알람 테스트 전용 API")
  @GetMapping(value = "/alarm/test")
  public ResultDataResponseDTO alarmTest(@RequestParam(value = "msg") String msg) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    memberService.alarmTest(msg, currentMemberId);
    return ResultDataResponseDTO.empty();
  }

}
