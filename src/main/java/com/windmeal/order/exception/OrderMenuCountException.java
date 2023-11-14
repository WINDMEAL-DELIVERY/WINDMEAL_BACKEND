package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderMenuCountException extends GeneralException {


  public OrderMenuCountException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderMenuCountException(String s) {
    super(s);
  }
}
