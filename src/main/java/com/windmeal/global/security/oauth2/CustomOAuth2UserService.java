package com.windmeal.global.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    /*
    인증이 성공했을 때 호출될 메서드
    여기서 회원 여부를 확인하고, 회원이 아니라면 가입까지 진행시킨다.
    추가로 학교 이메일로 가입한 것인지도 확인한다.
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }
}
