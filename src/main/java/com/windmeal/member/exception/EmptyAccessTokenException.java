package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EmptyAccessTokenException extends GeneralException {
    public EmptyAccessTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
