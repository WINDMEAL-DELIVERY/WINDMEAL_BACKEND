package com.windmeal.global.token.dao;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface RefreshTokenDAO {
    void createRefreshToken(String key, String refreshToken, String ip) throws Exception;
    Optional<String> getRefreshToken(String key);
    void removeRefreshToken(String key);
}
