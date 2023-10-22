package com.windmeal.store.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class MenuCategoryNotFoundException extends GeneralException {



    public MenuCategoryNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MenuCategoryNotFoundException(String s) {
        super(s);
    }
}
