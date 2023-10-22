package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MenuNotFoundException extends GeneralException {



    public MenuNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MenuNotFoundException(String s) {
        super(s);
    }
}
