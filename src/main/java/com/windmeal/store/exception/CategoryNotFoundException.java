package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class CategoryNotFoundException extends GeneralException {


  public CategoryNotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public CategoryNotFoundException(String s) {
    super(ErrorCode.NOT_FOUND,s);
  }

  public CategoryNotFoundException() {
    super(ErrorCode.NOT_FOUND,"카테고리가 존재하지 않습니다.");
  }
}
