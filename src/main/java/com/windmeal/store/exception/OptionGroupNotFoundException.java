package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class OptionGroupNotFoundException extends GeneralException {



    public OptionGroupNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OptionGroupNotFoundException(String s) {
        super(s);
    }
}
