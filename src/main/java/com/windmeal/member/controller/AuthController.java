package com.windmeal.member.controller;

import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.token.dto.ReissueResponse;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.global.util.CookieUtil;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.windmeal.global.constants.JwtConstants.ACCESSTOKEN;
import static com.windmeal.global.constants.JwtConstants.ACCESS_TOKEN_EXPIRES_IN;
import static com.windmeal.global.constants.JwtConstants.AUTHORIZATION_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "인증", description = "인증(토큰) 관련 API 입니다.")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "토큰 리이슈 요청", description = "토큰을 재발급합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레쉬 토큰, 재로그인 요망"),
        @ApiResponse(responseCode = "404", description = "엑세스 토큰이 존재하지 않음"),
        @ApiResponse(responseCode = "404", description = "리프레쉬 토큰이 존재하지 않음"),
        @ApiResponse(responseCode = "400", description = "최초 발급 사용자와 갱신 요청 사용자의 IP가 다름, 탈취 가능성 있음"),
        @ApiResponse(responseCode = "600", description = "서버 내부 JSON 파싱 에러"),
        @ApiResponse(responseCode = "600", description = "서버 내부 암호화 에러")
    })
    @PostMapping("/reissue")
    public ResultDataResponseDTO<ReissueResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getHeader(AUTHORIZATION_HEADER);
        String clientIpAddress = ClientIpUtil.getClientIpAddress(request);
        String accessToken = authService.reissue(Optional.ofNullable(code), clientIpAddress);
//        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        return ResultDataResponseDTO.of(ReissueResponse.of(accessToken));
    }

}
