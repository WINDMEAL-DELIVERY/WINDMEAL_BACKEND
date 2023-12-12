package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class StoreCategoryNotFoundException extends GeneralException {


  public StoreCategoryNotFoundException(String message) {
    super(ErrorCode.NOT_FOUND, message);
  }

  public StoreCategoryNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public StoreCategoryNotFoundException() {
    super(ErrorCode.NOT_FOUND, "존재하지 않는 카테고리입니다.");
  }
}
