package com.windmeal.global.token.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.ResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    accessDeniedException.printStackTrace();
    log.error(this.getClass().getName());
    String currentMemberEmail = SecurityUtil.getCurrentMemberEmail();
    ResponseDTO responseDTO = ResponseDTO.of(false, ErrorCode.FORBIDDEN,
        currentMemberEmail + "사용자는 해당 리소스에 접근할 권한이 없습니다.");
    String responseJSON = objectMapper.writeValueAsString(responseDTO);

    // 값을 반환해준다.
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(responseJSON);
  }
}
