package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class StoreNotFoundException extends GeneralException {


  public StoreNotFoundException(String message) {
    super(message);
  }

  public StoreNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
