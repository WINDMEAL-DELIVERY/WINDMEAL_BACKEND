package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidPlaceNameException extends GeneralException {
    public InvalidPlaceNameException() {
        super(ErrorCode.BAD_REQUEST, "유효하지 않은 장소입니다.");
    }
}
