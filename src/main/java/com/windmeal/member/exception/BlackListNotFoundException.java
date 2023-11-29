package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class BlackListNotFoundException extends GeneralException {
    public BlackListNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
