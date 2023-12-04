package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EncryptionException extends GeneralException {

  public EncryptionException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
