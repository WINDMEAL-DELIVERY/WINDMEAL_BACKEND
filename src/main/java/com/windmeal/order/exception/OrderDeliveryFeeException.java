package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderDeliveryFeeException extends GeneralException {


  public OrderDeliveryFeeException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public OrderDeliveryFeeException(String s) {
    super(ErrorCode.BAD_REQUEST,s);
  }
}
