package com.windmeal.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.chat.exception.NotMatchingIpAddressException;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dto.RefreshTokenResponse;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.exception.EmptyAccessTokenException;
import com.windmeal.member.exception.EmptyMemberInfoException;
import com.windmeal.member.exception.MemberNotMatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void reissue(Optional<Cookie> optionalAccessToken, Long currentMemberId, String currentMemberEmail, String clientIpAddress) {
        //
        Cookie accessTokenCookie = optionalAccessToken
                .orElseThrow(() -> new EmptyAccessTokenException(ErrorCode.BAD_REQUEST, "토큰 정보가 존재하지 않습니다."));
        String accessToken = accessTokenCookie.getValue();
        MemberInfoDTO memberInfoDTO = tokenProvider.getMemberInfoFromToken(accessToken)
                .orElseThrow(() -> new EmptyMemberInfoException(ErrorCode.BAD_REQUEST, "토큰으로 존재하는 사용자의 정보가 없습니다."));
        if(!memberInfoDTO.getId().equals(currentMemberId) || !memberInfoDTO.getEmail().equals(currentMemberEmail)) {
            throw new MemberNotMatchException(ErrorCode.VALIDATION_ERROR, "요청자와 토큰 소유자가 다릅니다.");
        }

        String refreshTokenString = refreshTokenDAO.getRefreshToken(memberInfoDTO.getId(), memberInfoDTO.getEmail());
        try {
            RefreshTokenResponse refreshTokenResponse = objectMapper.readValue(refreshTokenString, RefreshTokenResponse.class);
            if(!refreshTokenResponse.getClientIp().equals(clientIpAddress)) {
                throw new NotMatchingIpAddressException(ErrorCode.VALIDATION_ERROR, "IP 주소가 다릅니다. 탈취된 토큰일 가능성이 있습니다.");
            }
            // 지금까지는 검증, 이제 리이슈 시작.

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

