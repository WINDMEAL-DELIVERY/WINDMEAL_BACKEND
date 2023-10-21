package com.windmeal.global.token.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import static com.windmeal.global.constants.JwtConstants.PREFIX_REFRESHTOKEN;

@RequiredArgsConstructor
public class RefreshTokenDAOImpl implements RefreshTokenDAO {
    private final RedisTemplate RedisTemplate;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiresIn;
    @Override
    public void createRefreshToken(Long memberId, String refreshToken) {
        RedisTemplate.opsForValue().set(PREFIX_REFRESHTOKEN + memberId
                , refreshToken
                , Duration.ofSeconds(refreshTokenExpiresIn));
    }

    @Override
    public String getRefreshToken(Long memberId) {
        return (String) RedisTemplate.opsForValue().get(PREFIX_REFRESHTOKEN + memberId);
    }

    @Override
    public void removeRefreshToken(Long memberId) {
        RedisTemplate.delete(PREFIX_REFRESHTOKEN + memberId);
    }
}

