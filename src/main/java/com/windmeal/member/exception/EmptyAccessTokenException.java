package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EmptyAccessTokenException extends GeneralException {
    public EmptyAccessTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public EmptyAccessTokenException() {
        super(ErrorCode.NOT_FOUND, "엑세스 토큰 정보가 존재하지 않습니다.");
    }
}
