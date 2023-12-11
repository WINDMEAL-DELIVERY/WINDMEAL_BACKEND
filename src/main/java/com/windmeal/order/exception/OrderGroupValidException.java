package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderGroupValidException extends GeneralException {

  public OrderGroupValidException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderGroupValidException() {
    super(ErrorCode.BAD_REQUEST,"옵션 선택이 잘못되었습니다.");
  }
  public OrderGroupValidException(String s) {
    super(ErrorCode.BAD_REQUEST,s);
  }
}
