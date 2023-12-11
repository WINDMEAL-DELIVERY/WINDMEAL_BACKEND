package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class BlackListNotFoundException extends GeneralException {
    public BlackListNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BlackListNotFoundException() {
        super(ErrorCode.NOT_FOUND, "해당 차단은 존재하지 않습니다.");
    }
}
