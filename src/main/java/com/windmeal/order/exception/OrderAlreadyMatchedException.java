package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderAlreadyMatchedException extends GeneralException {
    public OrderAlreadyMatchedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OrderAlreadyMatchedException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    public OrderAlreadyMatchedException() {
        super(ErrorCode.BAD_REQUEST,"이미 매칭된 주문입니다.");
    }
}
