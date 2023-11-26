package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MenuChangeException extends GeneralException {


  public MenuChangeException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public MenuChangeException(String s) {
    super(s);
  }
}
