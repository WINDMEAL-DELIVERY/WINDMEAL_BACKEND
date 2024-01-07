package com.windmeal.global.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ClientIpUtil {

    /*
        refresh token의 탈취에 대응하기 위해 토큰 발급 시점 사용자의 ip를 함께 저장한다.
        또한 토큰 재발급 상황에서도 사용자의 request 로부터 ip를 얻어와 비교 검증을 수행해야 한다.
        위의 필요성으로 유틸 클래스를 만들게 되었다.
     */

  /**
   * request.getRemoteAddr()는 WAS 앞단에 프록시나 로드밸런서가 위치할 경우 우리가 의도한 대로 동작하지 않는다. 이럴 때는 XFF 헤더를 조사하여 키
   * 값을 가져오면 되고, 경우에 따라 다른 헤더에 ip가 저장될 수 있다. 따라서 아래와 같이 작성되어야 한다.
   *
   * @param request
   * @return String
   */
  public static String getClientIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }
}
