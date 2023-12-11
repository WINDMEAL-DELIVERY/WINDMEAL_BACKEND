package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrdererMissMatchException extends GeneralException {
    public OrdererMissMatchException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    public OrdererMissMatchException() {
        super(ErrorCode.BAD_REQUEST, "본인의 주문만 삭제할 수 있습니다.");
    }
}
