package com.windmeal.global.exception;

public class AesException extends GeneralException {


  public AesException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
