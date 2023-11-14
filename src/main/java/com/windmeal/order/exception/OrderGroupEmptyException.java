package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderGroupEmptyException extends GeneralException {


  public OrderGroupEmptyException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderGroupEmptyException(String s) {
    super(s);
  }
}
