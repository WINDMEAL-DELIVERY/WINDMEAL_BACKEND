package com.windmeal.chat.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidRequesterException extends GeneralException {
    public InvalidRequesterException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
