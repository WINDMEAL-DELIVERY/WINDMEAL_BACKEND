package com.windmeal.global.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.windmeal.global.security.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.windmeal.global.security.oauth2.user.UserPrincipal;
import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dto.TokenInfoResponse;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.AES256Util;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.windmeal.global.constants.CookieConstants.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.windmeal.global.constants.JwtConstants.ACCESSTOKEN;
import static com.windmeal.global.constants.JwtConstants.PREFIX_REFRESHTOKEN;
import static com.windmeal.global.constants.SecurityConstants.*;


@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final AES256Util aes256Util;
  private final TokenProvider tokenProvider;
  private final RefreshTokenDAO refreshTokenDao;
  private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication)
      throws IOException {

    Optional<String> result = addToken(request, response, authentication);
    // 토큰을 추가하는 과정에서 에러가 발생하면 리다이렉트를 하는데, 이때 로직이 종료되지 않고 흘러가기 때문에 명시적으로 종료해줘야 한다.
    // 그러기 위해서 결과를 반환값으로 받고, 이에 따른 처리를 해주겠다.
    if (response.isCommitted() || result.isEmpty()) {
      return;
    }
    setDefaultTargetUrl(determineTargetUrl(request, response, authentication, result.get()));
    clearAuthenticationAttributes(request, response);
    log.info("성공");
    getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
  }

  private Optional<String> addToken(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    // 닉네임이 존재할 경우 클레임에 포함하고, 그렇지 않은 경우 클레임에 포함하지 않는다.
    TokenInfoResponse.TokenDetail tokenDTO = tokenProvider.createToken(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token;
    try {
      String clientIp = ClientIpUtil.getClientIpAddress(request);
      saveRefreshTokenInStorage(
          tokenDTO.getRefreshToken(),
          Long.valueOf(authentication.getName()),
          userDetails.getPassword(),
          clientIp
      );
      token = aes256Util.encrypt(tokenDTO.getAccessToken());
    } catch (Exception e) {
      CookieUtil.deleteCookie(request, response, ACCESSTOKEN);
      response.sendRedirect(ERROR_REDIRECT);
      token = null;
    }
    return Optional.ofNullable(token);
  }


  /**
   * {@link com.windmeal.global.security.oauth2.impl.CustomOAuth2UserService} 에서 데이터베이스에 접근하며 사용자의
   * 닉네임을 확인함. 닉네임이 존재한다면 attribute 객체에 추가해주고, 해당 인스턴스를 매개변수로 전달하여 authentication 객체를 생성함. 그리고 동일한
   * authentication 객체를 활용하여 현재 위치에서 데이터베이스에 추가적인 접근 없이 사용자의 가입 여부를 알 수 있음
   *
   * @param request
   * @param response
   * @param authentication
   * @return
   * @author Owen Choi
   */
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication, String encryptedToken) {
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    Map<String, Object> attributes = principal.getAttributes();
    Optional<String> nickname = Optional.ofNullable(
        (String) attributes.getOrDefault(NICKNAME_KEY, null));
    Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);
    String resultUri =
        nickname.isPresent() ? redirectUri.orElse(DEFAULT_OAUTH_REDIRECT) : NICKNAME_REDIRECT;
    String uriString = UriComponentsBuilder.fromUriString(resultUri)
        .queryParam("code", encryptedToken)
        .build().toUriString();
    return uriString;
  }

  private void saveRefreshTokenInStorage(String refreshToken, Long memberId, String email,
      String ip) throws Exception {
    String encrypt = aes256Util.encrypt(refreshToken);
    String key = aes256Util.encrypt(PREFIX_REFRESHTOKEN + memberId + email);
    refreshTokenDao.createRefreshToken(key, encrypt, ip);
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

}