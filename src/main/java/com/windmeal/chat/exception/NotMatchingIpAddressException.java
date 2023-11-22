package com.windmeal.chat.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class NotMatchingIpAddressException extends GeneralException {
    public NotMatchingIpAddressException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
