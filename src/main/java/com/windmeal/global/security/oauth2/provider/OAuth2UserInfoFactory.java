package com.windmeal.global.security.oauth2.provider;

import com.windmeal.global.security.oauth2.user.GoogleOAuth2UserInfo;
import com.windmeal.global.security.oauth2.user.OAuth2UserInfo;

import java.util.Map;

import static com.windmeal.global.constants.OAuth2Provider.GOOGLE;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String providerName, Map<String, Object> attributes) {
        switch (providerName) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            default:
                throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
