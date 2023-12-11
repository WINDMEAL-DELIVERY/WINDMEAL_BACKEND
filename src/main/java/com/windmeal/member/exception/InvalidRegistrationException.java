package com.windmeal.member.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

/**
 * 닉네임을 등록하지 않은 사용자가 토큰 재발급을 요청할때 발생할 수 있는 예외이다.
 * 닉네임 등록 까지가 회원가입이기 때문에, 닉네임이 없다느 것은 우리 서비스의 회원이 아니라는 뜻이다.
 */
public class InvalidRegistrationException extends GeneralException {
  public InvalidRegistrationException(ErrorCode errorCode,
      String message) {
    super(errorCode, message);
  }
}
