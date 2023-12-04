package com.windmeal.global.token.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.token.dto.RefreshTokenResponse;
import com.windmeal.global.util.AES256Util;
import com.windmeal.global.util.ClientIpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Optional;

import static com.windmeal.global.constants.JwtConstants.PREFIX_REFRESHTOKEN;
import static com.windmeal.global.constants.JwtConstants.REFRESH_TOKEN_EXPIRES_IN;

@RequiredArgsConstructor
public class RefreshTokenDAOImpl implements RefreshTokenDAO {

  private final ObjectMapper objectMapper;
  private final RedisTemplate redisTemplate;


  /**
   * 암호화된 토큰과 키를 refresh token을 redis에 저장한다.
   * @param key
   * @param refreshToken
   * @param clientIp
   * @throws Exception
   */
  @Override
  public void createRefreshToken(String key, String refreshToken, String clientIp)
      throws Exception {
    String value = objectMapper.writeValueAsString(RefreshTokenResponse.of(refreshToken, clientIp));
    redisTemplate.opsForValue().set(key
        , value
        , Duration.ofSeconds(REFRESH_TOKEN_EXPIRES_IN));
  }

  @Override
  public Optional<String> getRefreshToken(String key) throws Exception{
    return Optional.ofNullable(
        (String) redisTemplate.opsForValue().get(key));
  }

  @Override
  public void removeRefreshToken(Long memberId, String email) {
    redisTemplate.delete(PREFIX_REFRESHTOKEN + memberId + email);
  }
}

