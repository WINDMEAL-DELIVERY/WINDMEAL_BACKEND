package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MemberNotFoundException extends GeneralException {

    public MemberNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }

    public MemberNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MemberNotFoundException() {
        super(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다.");
    }
}
