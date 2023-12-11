package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class StoreNotOpenException extends GeneralException {


  public StoreNotOpenException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public StoreNotOpenException(String s) {
    super(ErrorCode.BAD_REQUEST,s);
  }

  public StoreNotOpenException() {
    super(ErrorCode.BAD_REQUEST, "가게 운영시간이 아닙니다.");
  }
}
