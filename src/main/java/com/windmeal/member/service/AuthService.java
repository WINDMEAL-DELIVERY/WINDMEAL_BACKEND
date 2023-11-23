package com.windmeal.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.chat.exception.NotMatchingIpAddressException;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;
import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dto.RefreshTokenResponse;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenDAO refreshTokenDAO;
    private final ObjectMapper objectMapper;

    public String reissue(Optional<Cookie> optionalAccessToken, String clientIpAddress) {
        // 쿠키에 엑세스 토큰이 정상적으로 존재하는지 검증
        Cookie accessTokenCookie = optionalAccessToken
                .orElseThrow(() -> new EmptyAccessTokenException(ErrorCode.NOT_FOUND, "엑세스 토큰 정보가 존재하지 않습니다."));

        String accessToken = accessTokenCookie.getValue();

        Authentication authenticationForReissue = tokenProvider.getAuthentication(accessToken);
        UserDetails principal = (UserDetails) authenticationForReissue.getPrincipal();
        Long id = Long.parseLong(principal.getUsername());
        String email = principal.getPassword();

        // 사용자의 정보로 구성된 key로 refresh token을 조회할 수 있는지 검증 (리프레쉬 토큰이 존재하는가?)
        String refreshTokenString = refreshTokenDAO.getRefreshToken(id, email)
                .orElseThrow(() -> new EmptyRefreshTokenException(ErrorCode.NOT_FOUND, "리프레쉬 토큰 정보가 존재하지 않습니다."));

        // 재발급을 요청한 사용자가 최초로 요청한 사용자의 IP와 일치하는가? (탈취 방지)
        try {
            RefreshTokenResponse refreshTokenResponse = objectMapper.readValue(refreshTokenString, RefreshTokenResponse.class);

            // IP 검증 전에 리프레쉬 토큰 자체에 대해서 먼저 검증을 수행해야 한다.
            if(!tokenProvider.validateToken(refreshTokenResponse.getRefreshToken())) {
                throw new InvalidRefreshTokenException(ErrorCode.UNAUTHORIZED, "유효하지 않은 리프레쉬 토큰입니다. 다시 로그인해주세요.");
            }

            if(!refreshTokenResponse.getClientIp().equals(clientIpAddress)) {
                throw new NotMatchingIpAddressException(ErrorCode.VALIDATION_ERROR, "IP 주소가 다릅니다. 탈취된 토큰일 가능성이 있습니다.");
            }

            return tokenProvider.createAccessToken(authenticationForReissue);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new GeneralException(ErrorCode.INTERNAL_ERROR, "JSON 파싱 에러");
        }
    }
}

