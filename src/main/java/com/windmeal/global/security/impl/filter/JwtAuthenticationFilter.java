package com.windmeal.global.security.impl.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    private final ObjectMapper objectMapper;


    public JwtAuthenticationFilter(TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 먼저 해더를 조사한다. 그리고 유효한 토큰 문자열이 있다면 가져온다.
        String accessToken = resolveToken(request);
        if(StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            try{
                Authentication authenticated = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            } catch (AuthenticationException authenticationException) {
                authenticationException.printStackTrace();
                log.error("인증 실패 - JwtAuthenticationFilter");
                SecurityContextHolder.clearContext();
                sendResponse(response, authenticationException);
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

    private void sendResponse(HttpServletResponse response, AuthenticationException authenticationException) {
        response.setStatus(ErrorCode.UNAUTHORIZED.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ExceptionResponseDTO responseDTO = ExceptionResponseDTO.of(ErrorCode.UNAUTHORIZED, authenticationException.getMessage());
        try{
            response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}