package com.windmeal.member.service;

import static com.windmeal.global.constants.JwtConstants.PREFIX_REFRESHTOKEN;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.chat.exception.NotMatchingIpAddressException;
import com.windmeal.global.exception.AesException;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;
import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dto.RefreshTokenResponse;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.AES256Util;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.member.domain.Member;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.exception.*;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Optional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final AES256Util aes256Util;
  private final ObjectMapper objectMapper;
  private final TokenProvider tokenProvider;
  private final RefreshTokenDAO refreshTokenDAO;
  private final MemberRepository memberRepository;

  public String reissue(Optional<String> code, String clientIpAddress) {

    String bearerToken = code
        .orElseThrow(
            () -> new EmptyAccessTokenException(ErrorCode.NOT_FOUND, "엑세스 토큰 정보가 존재하지 않습니다."));
    String accessToken = bearerToken.substring(7);
    try {
      accessToken = aes256Util.decrypt(accessToken);
      Authentication authenticationForReissue = tokenProvider.getAuthentication(accessToken);
      UserDetails principal = (UserDetails) authenticationForReissue.getPrincipal();
      Long id = Long.parseLong(principal.getUsername());
      String email = principal.getPassword();

      Member member = memberRepository.findById(id).orElseThrow(
          () -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "해당되는 사용자가 존재하지 않습니다."));

      if(!StringUtils.hasText(member.getNickname())) {
        throw new InvalidRegistrationException(ErrorCode.VALIDATION_ERROR, "닉네임이 등록되지 않은 사용자입니다.");
      }

      // 사용자의 정보로 구성된 key로 refresh token을 조회할 수 있는지 검증 (리프레쉬 토큰이 존재하는가?)
      String key = aes256Util.encrypt(PREFIX_REFRESHTOKEN + id + email);
      String refreshTokenString = refreshTokenDAO.getRefreshToken(key)
          .orElseThrow(
              () -> new EmptyRefreshTokenException(ErrorCode.NOT_FOUND, "리프레쉬 토큰 정보가 존재하지 않습니다."));

      // 재발급을 요청한 사용자가 최초로 요청한 사용자의 IP와 일치하는가? (탈취 방지)
      RefreshTokenResponse refreshTokenResponse = objectMapper.readValue(refreshTokenString,
          RefreshTokenResponse.class);

      // IP 검증 전에 리프레쉬 토큰 자체에 대해서 먼저 검증을 수행해야 한다.
      // 암호화된 리프레쉬 토큰을 다시 복호화 한다.
      String decrypt = aes256Util.decrypt(refreshTokenResponse.getRefreshToken());
      if (!tokenProvider.validateToken(decrypt)) {
        throw new InvalidRefreshTokenException(ErrorCode.UNAUTHORIZED,
            "유효하지 않은 리프레쉬 토큰입니다. 다시 로그인해주세요.");
      }

      if (!refreshTokenResponse.getClientIp().equals(clientIpAddress)) {
        throw new NotMatchingIpAddressException(ErrorCode.VALIDATION_ERROR,
            "IP 주소가 다릅니다. 탈취된 토큰일 가능성이 있습니다.");
      }

      // 위의 검증 절차를 모두 통과하였으면 토큰을 새로 발급한다.
      String rowToken = tokenProvider.createAccessToken(authenticationForReissue, member.getNickname());
      return aes256Util.encrypt(rowToken);

    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new GeneralException(ErrorCode.INTERNAL_ERROR, "JSON 파싱 에러");
    } catch (AesException e) {
      e.printStackTrace();
      throw new EncryptionException(e.getErrorCode(), "암호화 과정에서 오류가 발생했습니다.");
    }
  }
}

