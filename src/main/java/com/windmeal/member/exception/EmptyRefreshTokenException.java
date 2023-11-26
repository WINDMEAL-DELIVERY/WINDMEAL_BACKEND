package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EmptyRefreshTokenException extends GeneralException {
    public EmptyRefreshTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
