package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MemberNotFoundException extends GeneralException {

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
