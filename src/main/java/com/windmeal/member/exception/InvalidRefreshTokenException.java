package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidRefreshTokenException extends GeneralException {
    public InvalidRefreshTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidRefreshTokenException() {
        super(ErrorCode.UNAUTHORIZED,
            "유효하지 않은 리프레쉬 토큰입니다. 다시 로그인해주세요.");
    }
}
