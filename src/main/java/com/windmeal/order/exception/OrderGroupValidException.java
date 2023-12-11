package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderGroupValidException extends GeneralException {

  public OrderGroupValidException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderGroupValidException(String s) {
    super(s);
  }
}
