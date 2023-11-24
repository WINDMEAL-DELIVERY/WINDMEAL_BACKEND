package com.windmeal.member.controller;

import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.dto.request.NicknameRequest;
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
            @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임"),
            @ApiResponse(responseCode = "404", description = "해당되는 사용자가 존재하지 않음")
    })
    @PostMapping(value = "/nickname")
    public ResultDataResponseDTO<String> registerNickname(@Valid @RequestBody NicknameRequest request) {
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

}
