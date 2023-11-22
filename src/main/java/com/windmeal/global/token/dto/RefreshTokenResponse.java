package com.windmeal.global.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private String refreshToken;
    private String clientIp;

    public static RefreshTokenResponse of(String refreshToken, String clientIp) {
        return RefreshTokenResponse.builder()
                .refreshToken(refreshToken)
                .clientIp(clientIp)
                .build();
    }
}
