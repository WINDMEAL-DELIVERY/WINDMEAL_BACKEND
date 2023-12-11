package com.windmeal.order.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OrderMenuCountException extends GeneralException {


  public OrderMenuCountException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
  public OrderMenuCountException() {
    super(ErrorCode.BAD_REQUEST, "메뉴의 수량을 1개 이상 입력해주세요.");
  }
  public OrderMenuCountException(String s) {
    super(ErrorCode.BAD_REQUEST, s);
  }
}
