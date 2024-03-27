package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class NotProcessingDeliveryException extends GeneralException {
    public NotProcessingDeliveryException() {
        super(ErrorCode.BAD_REQUEST, "진행 중인 배달이 아닙니다.");
    }
}
