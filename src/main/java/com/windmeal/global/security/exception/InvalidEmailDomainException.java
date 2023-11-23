package com.windmeal.global.security.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;
import org.springframework.security.core.AuthenticationException;

public class InvalidEmailDomainException extends AuthenticationException {

    public InvalidEmailDomainException(String message) {
        super(message);
    }
}
