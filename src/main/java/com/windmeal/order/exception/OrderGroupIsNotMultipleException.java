package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderGroupIsNotMultipleException extends GeneralException {


  public OrderGroupIsNotMultipleException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderGroupIsNotMultipleException(String s) {
    super(s);
  }
}
