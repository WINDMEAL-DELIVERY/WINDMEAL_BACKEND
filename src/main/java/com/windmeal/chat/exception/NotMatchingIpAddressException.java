package com.windmeal.chat.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class NotMatchingIpAddressException extends GeneralException {
    public NotMatchingIpAddressException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotMatchingIpAddressException() {
        super(ErrorCode.VALIDATION_ERROR,
            "IP 주소가 다릅니다. 탈취된 토큰일 가능성이 있습니다.");
    }
}
