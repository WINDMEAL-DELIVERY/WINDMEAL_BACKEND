package com.windmeal.member.controller;

import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.global.util.CookieUtil;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.service.AuthService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResultDataResponseDTO reissue(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 accessToken을 가져온 뒤, 여기서 정보를 가져와서 refreshToken의 key로 조합한 뒤 토큰을 찾는다.
        // 토큰이 존재하고 유효하다면 새로운 엑세스 토큰을 만들어 발급해주고, 그렇지 않다면 예외를 발생시킨다.
        Optional<Cookie> cookie = CookieUtil.getCookie(request, ACCESSTOKEN);
        String clientIpAddress = ClientIpUtil.getClientIpAddress(request);
        String accessToken = authService.reissue(cookie, clientIpAddress);
        CookieUtil.deleteCookie(request, response, ACCESSTOKEN);
        CookieUtil.addCookie(response, ACCESSTOKEN, accessToken, ACCESS_TOKEN_EXPIRES_IN);
        return ResultDataResponseDTO.empty();
    }

}
