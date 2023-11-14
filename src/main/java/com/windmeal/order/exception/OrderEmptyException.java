package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderEmptyException extends GeneralException {


  public OrderEmptyException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderEmptyException(String s) {
    super(s);
  }
}
