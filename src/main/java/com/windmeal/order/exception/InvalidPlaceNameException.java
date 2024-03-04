package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class InvalidPlaceNameException extends GeneralException {
    public InvalidPlaceNameException() {
        super(ErrorCode.BAD_REQUEST, "이미 존재하는 장소명입니다.");
    }
}
