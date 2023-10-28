package com.windmeal.global.security.impl.filter;


import com.windmeal.global.token.util.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.windmeal.global.constants.JwtConstants.AUTHORIZATION_HEADER;
import static com.windmeal.global.constants.JwtConstants.BEARER_PREFIX;

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
                // jwt로부터 인증 객체를 생성한다. (이 시점에서의 인증 객체는 아직 인증이 되지 않은 상태이다.)
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                /*
                    authenticationManager의 authenticate 메서드를 실행하여 인증 객체에 대해 인증을 수행한다.
                    내부적으로 provider manager (authenticationManager의 구현체)가 알맞은 provider를 찾아서 인증을 진행한다.
                    windmeal에서는 provider를 따로 커스텀하여 사용하지 않고, usernamePasswordAuthenticationFilter를 타기 때문에
                    해당 부분을 담당하는 provider가 호출될 것이다.
                 */
                Authentication authenticated = authenticationManager.authenticate(authentication);
                // 인증이 완료된 인증 객체에 대해서 security session (context)에 저장해준다.
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            } catch (AuthenticationException authenticationException) {
                log.error("인증 실패 - JwtAuthenticationFilter");
                SecurityContextHolder.clearContext();
            }
        }
        // TODO 헤더가 resolveToken의 반환값이 null인 경우에 대한 처리?
        filterChain.doFilter(request, response);
    }

    /**
     * HttpRequest를 까보고, 인증 관련 헤더가 존재함과 동시에 토큰의 형태를 정상적으로 지니고 있다면 인증을 위한 부분을 반환한다.
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}