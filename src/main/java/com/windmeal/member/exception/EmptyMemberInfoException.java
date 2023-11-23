package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EmptyMemberInfoException extends GeneralException {
    public EmptyMemberInfoException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
