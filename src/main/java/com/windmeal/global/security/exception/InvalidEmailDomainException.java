package com.windmeal.global.security.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class InvalidEmailDomainException extends OAuth2AuthenticationException {

    public InvalidEmailDomainException(OAuth2Error error) {
        super(error);
    }
}
