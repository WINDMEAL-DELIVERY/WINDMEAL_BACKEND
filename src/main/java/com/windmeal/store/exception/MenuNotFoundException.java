package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MenuNotFoundException extends GeneralException {


  public MenuNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public MenuNotFoundException(String s) {
    super(ErrorCode.NOT_FOUND, s);
  }

  public MenuNotFoundException() {
    super(ErrorCode.NOT_FOUND, "메뉴가 존재하지 않습니다.");
  }
}
