package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderSpecificationChangeException extends GeneralException {
    public OrderSpecificationChangeException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OrderSpecificationChangeException() {
        super(ErrorCode.BAD_REQUEST,"메뉴 상세 정보가 변경되었습니다.");
    }
}
