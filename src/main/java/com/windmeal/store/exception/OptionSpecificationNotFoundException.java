package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OptionSpecificationNotFoundException extends GeneralException {



    public OptionSpecificationNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OptionSpecificationNotFoundException(String s) {
        super(s);
    }
}
