package com.windmeal.global.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDetail{
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;
        private Long refreshTokenExpiresIn;
    }
}
