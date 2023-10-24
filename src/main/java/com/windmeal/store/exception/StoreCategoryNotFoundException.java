package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class StoreCategoryNotFoundException extends GeneralException {


  public StoreCategoryNotFoundException(String message) {
    super(message);
  }

  public StoreCategoryNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
