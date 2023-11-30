package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class DeliveryNotFoundException extends GeneralException {
    public DeliveryNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
