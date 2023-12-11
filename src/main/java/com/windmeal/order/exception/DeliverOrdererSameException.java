package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class DeliverOrdererSameException extends GeneralException {
    public DeliverOrdererSameException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DeliverOrdererSameException() {
        super(ErrorCode.BAD_REQUEST,"본인의 주문을 배달할 수 없습니다.");
    }
}
