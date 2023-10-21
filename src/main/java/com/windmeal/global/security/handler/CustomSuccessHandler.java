package com.windmeal.global.security.handler;

import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dto.TokenDTO;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.windmeal.global.constants.CookieConstants.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.windmeal.global.constants.JwtConstants.ACCESSTOKEN;

/*
    CustomOAuth2UserInfo에서 인증이 완료된 사용자에 대해서 회원가입 처리를 해주었다.
    여기서는 확정 회원 (이전 과정에서 신규 회원은 가입까지 해주었으니)에 대해서 토큰을 발급한 뒤, 우리 서비스로 리다이렉트 시키는 부분이다.
 */

@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenDAO refreshTokenDao;
//    private final AuthorizationRequestRepository authorizationRequestRepository;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiresIn;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            return;
        }
//        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        //토큰 생성
        TokenDTO.TokenDetail tokenDTO = tokenProvider.createToken(authentication);
        saveRefreshTokenInStorage(tokenDTO.getRefreshToken(), Long.valueOf(authentication.getName()));
        CookieUtil.deleteCookie(request,response,ACCESSTOKEN);
        int accessTokenExpireTime;
        try{
            accessTokenExpireTime = Math.toIntExact(accessTokenExpiresIn);
        } catch(ArithmeticException e) {
            throw new ArithmeticException("쿠키의 유효 시간 범위가 잘못 설정되었습니다.");
        }
        CookieUtil.addCookie(response,ACCESSTOKEN,tokenDTO.getAccessToken(), accessTokenExpireTime);
        String uriString = UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
        return uriString;
    }

    private void saveRefreshTokenInStorage(String refreshToken, Long memberId) {
        refreshTokenDao.createRefreshToken(memberId, refreshToken);
    }

//    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        super.clearAuthenticationAttributes(request);
//        authorizationRequestRepository.removeAuthorizationRequest(request, response);
//    }
}
