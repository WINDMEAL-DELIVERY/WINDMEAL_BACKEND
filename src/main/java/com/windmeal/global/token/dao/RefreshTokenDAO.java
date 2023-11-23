package com.windmeal.global.token.dao;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RefreshTokenDAO {
    void createRefreshToken(Long memberId,String email, String refreshToken, String ip) throws JsonProcessingException;
    String getRefreshToken(Long memberId, String email);
    void removeRefreshToken(Long memberId, String email);
}
