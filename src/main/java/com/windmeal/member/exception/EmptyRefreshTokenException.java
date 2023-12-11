package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EmptyRefreshTokenException extends GeneralException {
    public EmptyRefreshTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public EmptyRefreshTokenException() {
        super(ErrorCode.NOT_FOUND, "리프레쉬 토큰 정보가 존재하지 않습니다.");
    }
}
