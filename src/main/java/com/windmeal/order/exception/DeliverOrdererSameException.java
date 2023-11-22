package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class DeliverOrdererSameException extends GeneralException {
    public DeliverOrdererSameException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
