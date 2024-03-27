package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidDeliverException extends GeneralException {
    public InvalidDeliverException() {
        super(ErrorCode.BAD_REQUEST, "배달원이 일치하지 않습니다.");
    }
}
