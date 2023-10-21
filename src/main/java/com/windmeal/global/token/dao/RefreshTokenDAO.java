package com.windmeal.global.token.dao;

public interface RefreshTokenDAO {
    void createRefreshToken(Long memberId,String refreshToken);
    String getRefreshToken(Long memberId);
    void removeRefreshToken(Long memberId);
}
