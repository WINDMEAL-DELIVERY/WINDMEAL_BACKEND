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
import static com.windmeal.global.constants.JwtConstants.AUTHORIZATION_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResultDataResponseDTO reissue(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getHeader(AUTHORIZATION_HEADER);
        String clientIpAddress = ClientIpUtil.getClientIpAddress(request);
        String accessToken = authService.reissue(Optional.ofNullable(code), clientIpAddress);
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        return ResultDataResponseDTO.empty();
    }

}
