package com.windmeal.global.constants;

public class JwtConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORITIES_KEY = "auth";
    public static final String PREFIX_REFRESHTOKEN = "refreshToken:";
    public static final String EMAIL = "email";

    // 30분
    public static final int ACCESS_TOKEN_EXPIRES_IN = 1000 * 60 * 30;
//    public static final int ACCESS_TOKEN_EXPIRES_IN = 1000 * 60;

    // 7일
    public static final long REFRESH_TOKEN_EXPIRES_IN = 1000 * 60 * 60 * 24;
//    public static final long REFRESH_TOKEN_EXPIRES_IN = 1000 * 60 * 2;
    public static final String ACCESSTOKEN = "token";
}
