package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderNotFoundException extends GeneralException {
    public OrderNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OrderNotFoundException() {
        super(ErrorCode.NOT_FOUND, "존재하지 않는 주문입니다.");
    }
}
