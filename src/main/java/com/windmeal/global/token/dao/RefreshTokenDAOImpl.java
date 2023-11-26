package com.windmeal.global.token.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.token.dto.RefreshTokenResponse;
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
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;


    /**
     * RefreshToken을 설정해주는 부분이다.
     * @param memberId
     * @param email 채팅서버에서 jwt만으로 사용자의 정보를 얻고 refreshToken의 존재 여부를 알기 위해 이메일을 추가했다.
     * @param refreshToken
     */
    @Override
    public void createRefreshToken(Long memberId, String email, String refreshToken, String clientIp) throws JsonProcessingException {
        String value = objectMapper.writeValueAsString(RefreshTokenResponse.of(refreshToken, clientIp));
        redisTemplate.opsForValue().set(PREFIX_REFRESHTOKEN + memberId + email
                , value
                , Duration.ofSeconds(REFRESH_TOKEN_EXPIRES_IN));
    }

    @Override
    public Optional<String> getRefreshToken(Long memberId, String email) {
        return Optional.ofNullable((String) redisTemplate.opsForValue().get(PREFIX_REFRESHTOKEN + memberId + email));
    }

    @Override
    public void removeRefreshToken(Long memberId, String email) {
        redisTemplate.delete(PREFIX_REFRESHTOKEN + memberId + email);
    }
}

