package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class StoreNotOpenException extends GeneralException {


  public StoreNotOpenException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public StoreNotOpenException(String s) {
    super(s);
  }
}
