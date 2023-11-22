package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MemberNotMatchException extends GeneralException {
    public MemberNotMatchException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
