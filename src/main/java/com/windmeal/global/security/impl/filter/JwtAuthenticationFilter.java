package com.windmeal.global.security.impl.filter;


import static com.windmeal.global.constants.JwtConstants.AUTHORIZATION_HEADER;
import static com.windmeal.global.constants.JwtConstants.BEARER_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.AES256Util;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JwtAuthenticationFilter는 HTTP 요청을 가로채서, 헤더를 조사하여 토큰값을 얻어 AuthenticationToken, 즉 인증용 객체를 생성한다.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  // OncePerRequestFilter는 요청당 한번만 동작하는 필터
  private final AES256Util aes256Util;
  private final ObjectMapper objectMapper;
  private final TokenProvider tokenProvider;


  public JwtAuthenticationFilter(AES256Util aes256Util, ObjectMapper objectMapper,
      TokenProvider tokenProvider) {
    this.aes256Util = aes256Util;
    this.objectMapper = objectMapper;
    this.tokenProvider = tokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    // 먼저 해더를 조사한다. 그리고 유효한 토큰 문자열이 있다면 가져온다.

    try {
      String accessToken = resolveToken(request);
      if (accessToken != null) {
        String decrypt = aes256Util.decrypt(accessToken);
        if (StringUtils.hasText(decrypt) && tokenProvider.validateToken(decrypt)) {
          Authentication authenticated = tokenProvider.getAuthentication(decrypt);
          SecurityContextHolder.getContext().setAuthentication(authenticated);
        }
      }
    } catch (Exception e) {
      // 암호화 관련 에러가 발생하면 이 곳에서 걸린다.
      log.error(e.getClass().getName());
      sendResponse(response, e);
      return;
    }
    filterChain.doFilter(request, response);

  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }


  private void sendResponse(HttpServletResponse response, Exception exception) {
    response.setStatus(ErrorCode.UNAUTHORIZED.getCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    ExceptionResponseDTO responseDTO = ExceptionResponseDTO.of(ErrorCode.UNAUTHORIZED,
        exception.getMessage());
    log.error(exception.getMessage());
    try {
      response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}