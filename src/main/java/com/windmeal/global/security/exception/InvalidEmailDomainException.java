package com.windmeal.global.security.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidEmailDomainException extends GeneralException {

    public InvalidEmailDomainException(String message) {
        super(message);
    }

    public InvalidEmailDomainException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
