package com.windmeal.global.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.windmeal.global.token.dao.RefreshTokenDAO;
import com.windmeal.global.token.dto.TokenInfoResponse;
import com.windmeal.global.token.util.TokenProvider;
import com.windmeal.global.util.ClientIpUtil;
import com.windmeal.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.windmeal.global.constants.JwtConstants.ACCESSTOKEN;
import static com.windmeal.global.constants.JwtConstants.ACCESS_TOKEN_EXPIRES_IN;
import static com.windmeal.global.constants.SecurityConstants.ERROR_REDIRECT;
import static com.windmeal.global.constants.SecurityConstants.OAUTH_REDIRECT;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        setDefaultTargetUrl(OAUTH_REDIRECT);
        // 토큰을 추가하는 과정에서 에러가 발생하면 리다이렉트를 하는데, 이때 로직이 종료되지 않고 흘러가기 때문에 명시적으로 종료해줘야 한다.
        // 그러기 위해서 결과를 반환값으로 받고, 이에 따른 처리를 해주겠다.
        boolean result = addTokenToCookie(request, response, authentication);
        if (response.isCommitted() || !result) {
            return;
        }
        getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
    }

    private boolean addTokenToCookie(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        TokenInfoResponse.TokenDetail tokenDTO = tokenProvider.createToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try{
            String clientIp = ClientIpUtil.getClientIpAddress(request);
            saveRefreshTokenInStorage(
                tokenDTO.getRefreshToken(),
                Long.valueOf(authentication.getName()),
                userDetails.getPassword(),
                clientIp
            );
            CookieUtil.deleteCookie(request,response,ACCESSTOKEN);
            CookieUtil.addCookie(response,ACCESSTOKEN,tokenDTO.getAccessToken(), ACCESS_TOKEN_EXPIRES_IN);
        } catch(JsonProcessingException e) {
            // 해당 로직이 통과되지 않고 예외가 발생하여 리다이렉트 하게 되면, 시큐리티 세션에는 정보가 저장되지 않기 때문에 세션은 삭제해주지 않아도 된다.
            CookieUtil.deleteCookie(request,response,ACCESSTOKEN);
            log.error("리프레쉬 토큰 직렬화 에러");
            response.sendRedirect(ERROR_REDIRECT);
            return false;
        }
        return true;
    }


    private void saveRefreshTokenInStorage(String refreshToken, Long memberId, String email, String ip) throws JsonProcessingException {
        refreshTokenDao.createRefreshToken(memberId, email, refreshToken, ip);
    }

}