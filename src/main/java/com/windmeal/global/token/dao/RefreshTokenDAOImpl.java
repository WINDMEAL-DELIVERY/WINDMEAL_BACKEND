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

    /**
     * RefreshToken을 설정해주는 부분이다.
     * @param memberId
     * @param email 채팅서버에서 jwt만으로 사용자의 정보를 얻고 refreshToken의 존재 여부를 알기 위해 이메일을 추가했다.
     * @param refreshToken
     */
    @Override
    public void createRefreshToken(Long memberId, String email, String refreshToken) {
        RedisTemplate.opsForValue().set(PREFIX_REFRESHTOKEN + memberId + email
                , refreshToken
                , Duration.ofSeconds(refreshTokenExpiresIn));
    }

    @Override
    public String getRefreshToken(Long memberId, String email) {
        return (String) RedisTemplate.opsForValue().get(PREFIX_REFRESHTOKEN + memberId + email);
    }

    @Override
    public void removeRefreshToken(Long memberId, String email) {
        RedisTemplate.delete(PREFIX_REFRESHTOKEN + memberId + email);
    }
}

