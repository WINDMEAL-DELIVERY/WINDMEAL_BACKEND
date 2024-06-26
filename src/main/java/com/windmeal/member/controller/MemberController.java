package com.windmeal.member.controller;

import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.dto.request.MemberInfoRequest;
import com.windmeal.member.dto.request.NicknameRequest;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.dto.response.MyPageDTO;
import com.windmeal.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Operation(summary = "닉네임 등록", description = "최초 회원가입 시, 닉네임을 추가로 입력 받습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "닉네임 설정 성공"),
      @ApiResponse(responseCode = "400", description = "이미 사용 중인 닉네임입니다.",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "닉네임에는 특수문자가 포함될 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @PostMapping(value = "/nickname")
  public ResultDataResponseDTO<String> registerNickname(
      @Valid @RequestBody NicknameRequest request) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    String nickname = memberService.registerNickname(request, currentMemberId);
    return ResultDataResponseDTO.of(nickname);
  }

  @Operation(summary = "닉네임 중복 확인", description = "닉네임이 존재하는지 확인합니다.")
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
      @ApiResponse(responseCode = "400", description = "토큰값은 반드시 존재해야 합니다.",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "600", description = "암호화 실패",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
  })
  @PostMapping(value = "/info")
  public ResultDataResponseDTO<MemberInfoDTO> tokenExchange(
      @Valid @RequestBody MemberInfoRequest memberInfoRequest) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    MemberInfoDTO memberInfo = memberService.memberInfoDetails(memberInfoRequest, currentMemberId);
    return ResultDataResponseDTO.of(memberInfo);
  }

  @Operation(summary = "사용자 정보 반환", description = "로그인 후 리다이렉트 하기 전 호출될 api")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
  })
  @GetMapping
  public ResultDataResponseDTO<MyPageDTO> myInfo() {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    return ResultDataResponseDTO.of(memberService.myInfoDetails(currentMemberId));
  }

  @Operation(summary = "사용자 프로필 이미지 반환", description = "사용자의 프로필 이미지를 반환")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
  })
  @GetMapping("/{memberId}/image")
  public ResultDataResponseDTO<String> userProfileImage(@PathVariable Long memberId) {
    return ResultDataResponseDTO.of(memberService.memberProfileImage(memberId));
  }


  @Operation(summary = "알람 테스트용 API", description = "프론트엔드 알람 테스트 전용 API")
  @GetMapping(value = "/alarm/test")
  public ResultDataResponseDTO alarmTest(
      @Parameter(description = "알람에 담을 메시지", required = false, schema = @Schema(example = "알람 테스트 메시지"))
      @RequestParam(value = "msg") String msg) {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    memberService.alarmTest(msg, currentMemberId);
    return ResultDataResponseDTO.empty();
  }

  @Operation(summary = "회원 탈퇴", description = "회원탈퇴 api")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
  })
  @DeleteMapping
  public ResultDataResponseDTO deleteAccount() {
    Long currentMemberId = SecurityUtil.getCurrentMemberId();
    memberService.deleteAccount(currentMemberId);
    return ResultDataResponseDTO.empty();
  }

}
