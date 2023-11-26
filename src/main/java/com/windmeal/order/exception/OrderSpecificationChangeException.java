package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderSpecificationChangeException extends GeneralException {
    public OrderSpecificationChangeException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
