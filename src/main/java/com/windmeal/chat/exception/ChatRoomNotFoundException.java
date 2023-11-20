package com.windmeal.chat.exception;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.exception.GeneralException;

public class ChatRoomNotFoundException extends GeneralException {


    public ChatRoomNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
