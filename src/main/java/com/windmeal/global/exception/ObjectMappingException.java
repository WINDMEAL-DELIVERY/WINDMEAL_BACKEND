package com.windmeal.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ObjectMappingException extends GeneralException {


  public ObjectMappingException(ErrorCode errorCode) {
    super(errorCode, "Json 내부 파싱 에러");
  }
}
