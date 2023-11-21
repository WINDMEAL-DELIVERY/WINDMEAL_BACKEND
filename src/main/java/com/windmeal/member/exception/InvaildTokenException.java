package com.windmeal.member.exception;

import org.springframework.security.core.AuthenticationException;

public class InvaildTokenException extends AuthenticationException {
    public InvaildTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvaildTokenException(String msg) {
        super(msg);
    }
}
