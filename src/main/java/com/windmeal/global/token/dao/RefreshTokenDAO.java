package com.windmeal.global.token.dao;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface RefreshTokenDAO {
    void createRefreshToken(Long memberId,String email, String refreshToken, String ip) throws JsonProcessingException;
    Optional<String> getRefreshToken(Long memberId, String email);
    void removeRefreshToken(Long memberId, String email);
}
