package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderGroupEmptyException extends GeneralException {


  public OrderGroupEmptyException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
  public OrderGroupEmptyException() {
    super(ErrorCode.BAD_REQUEST,"필수 옵션을 선택해주세요.");
  }
  public OrderGroupEmptyException(String s) {
    super(ErrorCode.BAD_REQUEST,s);
  }
}
