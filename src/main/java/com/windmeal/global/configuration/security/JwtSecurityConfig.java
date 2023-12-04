package com.windmeal.global.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.security.impl.filter.JwtAuthenticationFilter;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.AES256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final AES256Util aes256Util;
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;
    @Override
    public void configure(HttpSecurity http) {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(aes256Util, objectMapper, tokenProvider);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
