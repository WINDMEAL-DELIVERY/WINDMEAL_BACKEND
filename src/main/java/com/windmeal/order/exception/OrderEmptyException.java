package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderEmptyException extends GeneralException {


  public OrderEmptyException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
  public OrderEmptyException() {
    super(ErrorCode.BAD_REQUEST, "주문 항목이 비어있습니다.");
  }
  public OrderEmptyException(String s) {
    super(s);
  }
}
