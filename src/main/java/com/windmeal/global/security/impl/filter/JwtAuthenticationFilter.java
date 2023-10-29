package com.windmeal.global.security.impl.filter;


import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.windmeal.global.constants.JwtConstants.*;

/**
 * JwtAuthenticationFilter는 HTTP 요청을 가로채서, 헤더를 조사하여 토큰값을 얻어 AuthenticationToken, 즉 인증용 객체를 생성한다.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter는 요청당 한번만 동작하는 필터
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 먼저 해더를 조사한다. 그리고 유효한 토큰 문자열이 있다면 가져온다.
        String jwt = resolveToken(request);
        if(StringUtils.hasText(jwt)) {
            try{
                Authentication authenticated = tokenProvider.getAuthentication(jwt);
                /*
                    authenticationManager의 authenticate 메서드를 실행하여 인증 객체에 대해 인증을 수행한다.
                    내부적으로 provider manager (authenticationManager의 구현체)가 알맞은 provider를 찾아서 인증을 진행한다.
                    windmeal에서는 provider를 따로 커스텀하여 사용하지 않고, usernamePasswordAuthenticationFilter를 타기 때문에
                    해당 부분을 담당하는 provider가 호출될 것이다.
                 */
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            } catch (AuthenticationException authenticationException) {
                authenticationException.printStackTrace();
                log.error("인증 실패 - JwtAuthenticationFilter");
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 요청에 붙어서 넘어온 쿠키를 확인하여 토큰을 추출하는 메서드
     * @param request
     * @return 추출된 토큰값을 반환한다.
     */
    private String resolveToken(HttpServletRequest request) {
        Cookie token = CookieUtil.getCookie(request, ACCESSTOKEN).orElse(null);
        if(token != null) {
            return token.getValue();
        }
        return null;
    }
}
