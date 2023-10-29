package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class NotAuthorizedException extends GeneralException {

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
