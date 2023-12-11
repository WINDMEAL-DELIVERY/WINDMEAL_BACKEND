package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class DuplicatedNicknameException extends GeneralException {

    public DuplicatedNicknameException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DuplicatedNicknameException() {
        super(ErrorCode.VALIDATION_ERROR, "이미 사용 중인 닉네임입니다.");
    }
}
