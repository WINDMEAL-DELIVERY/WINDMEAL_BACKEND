package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidRefreshTokenException extends GeneralException {
    public InvalidRefreshTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
