package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class EncryptionException extends GeneralException {

  public EncryptionException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public EncryptionException(ErrorCode errorCode) {
    super("암호화 과정에서 오류가 발생했습니다.");
  }
}
