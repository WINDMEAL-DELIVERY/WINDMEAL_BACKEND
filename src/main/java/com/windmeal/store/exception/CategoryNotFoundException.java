package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class CategoryNotFoundException extends GeneralException {


    public CategoryNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public CategoryNotFoundException(String s) {
        super(s);
    }
}
