package com.windmeal.global.token.dao;

public interface RefreshTokenDAO {
    void createRefreshToken(Long memberId,String email, String refreshToken);
    String getRefreshToken(Long memberId, String email);
    void removeRefreshToken(Long memberId, String email);
}
