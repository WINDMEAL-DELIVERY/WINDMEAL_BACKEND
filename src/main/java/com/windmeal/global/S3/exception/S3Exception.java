package com.windmeal.global.S3.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class S3Exception extends GeneralException {
    public S3Exception(String message) {
        super(message);
    }

    public S3Exception(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
