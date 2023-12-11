package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderGroupIsNotMultipleException extends GeneralException {


  public OrderGroupIsNotMultipleException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
  public OrderGroupIsNotMultipleException() {
    super(ErrorCode.BAD_REQUEST,"1개의 옵션만 선택 가능합니다.");
  }

  public OrderGroupIsNotMultipleException(String s) {
    super(ErrorCode.BAD_REQUEST,s);
  }
}
