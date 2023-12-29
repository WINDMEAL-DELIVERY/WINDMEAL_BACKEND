package com.windmeal.global.security.handler;

import com.windmeal.global.security.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.windmeal.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

import static com.windmeal.global.constants.CookieConstants.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.windmeal.global.constants.SecurityConstants.ERROR_REDIRECT;


@Slf4j
@RequiredArgsConstructor
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 에러시 라다이렉트하는 URL의 경우는 프론트에서 정할 수 없다. 서버에서 정해줘야겠다.
//        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//                .map(Cookie::getValue)
//                .orElse(("/"));
        log.info("CustomFailureHandler, onAuthenticationFailure");
        String encode = Base64Utils.encodeToString(exception.getLocalizedMessage().getBytes());
//        byte[] encode = Base64Utils.encode(exception.getLocalizedMessage().getBytes());
        log.error(exception.getLocalizedMessage());
        String targetUrl = ERROR_REDIRECT;
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("msg", encode)
                .build().toUriString();

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
