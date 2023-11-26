package com.windmeal.global.token.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.info("JwtAuthenticationEntryPoint의 request 정보들");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                System.out.println("Header: " + headerName + " = " + headerValue);
            }
        }
        log.info("JwtAuthenticationEntryPoint의 request 정보들 - 끝");


        log.error(this.getClass().getName());
        authException.printStackTrace();
        ResponseDTO responseDTO = ResponseDTO.of(false, ErrorCode.UNAUTHORIZED, "사용자의 인증에 실패하였습니다.");
        String responseJSON = objectMapper.writeValueAsString(responseDTO);

        // 값을 반환해준다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseJSON);
    }
}
