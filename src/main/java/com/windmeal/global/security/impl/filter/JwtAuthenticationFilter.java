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

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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