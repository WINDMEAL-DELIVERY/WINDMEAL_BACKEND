package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class StoreNotFoundException extends GeneralException {


  public StoreNotFoundException(String message) {
    super(ErrorCode.NOT_FOUND, message);
  }

  public StoreNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public StoreNotFoundException() {
    super(ErrorCode.NOT_FOUND, "존재하지 않는 매장입니다.");
  }
}
