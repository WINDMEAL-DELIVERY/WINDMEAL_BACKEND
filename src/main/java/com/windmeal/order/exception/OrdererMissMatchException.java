package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrdererMissMatchException extends GeneralException {
    public OrdererMissMatchException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
