package com.windmeal.global.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.security.impl.filter.JwtAuthenticationFilter;
import com.windmeal.global.token.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    @Override
    public void configure(HttpSecurity http) {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(tokenProvider, authenticationManager, objectMapper);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
