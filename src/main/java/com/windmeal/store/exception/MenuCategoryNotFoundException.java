package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MenuCategoryNotFoundException extends GeneralException {


  public MenuCategoryNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public MenuCategoryNotFoundException(String s) {

    super(ErrorCode.NOT_FOUND, s);
  }

  public MenuCategoryNotFoundException() {
    super(ErrorCode.NOT_FOUND, "메뉴 카테고리가 존재하지 않습니다.");
  }
}
