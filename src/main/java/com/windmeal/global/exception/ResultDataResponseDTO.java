package com.windmeal.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResultDataResponseDTO<T> extends ResponseDTO  {

    private final T data;

    private ResultDataResponseDTO(T data) {
        super(true, ErrorCode.OK.getCode());
        this.data = data;
    }

    private ResultDataResponseDTO(T data, String message) {
        super(true, ErrorCode.OK.getCode(), message);
        this.data = data;
    }
    private ResultDataResponseDTO(T data, ErrorCode errorCode) {
        super(true, errorCode.getCode());
        this.data = data;
    }

    private ResultDataResponseDTO(T data, HttpStatus httpStatus) {
        super(true, httpStatus.value());
        this.data = data;
    }


    public static <T> ResultDataResponseDTO<T> of(T data) {
        return new ResultDataResponseDTO<>(data);
    }

    public static <T> ResultDataResponseDTO<T> of(T data,ErrorCode errorCode) {
        return new ResultDataResponseDTO<>(data,errorCode);
    }
    public static <T> ResultDataResponseDTO<T> of(T data, String message) {
        return new ResultDataResponseDTO<>(data, message);
    }

    public static <T> ResultDataResponseDTO<T> empty() {
        return new ResultDataResponseDTO<>(null);
    }

    public static <T> ResultDataResponseDTO<T> empty(ErrorCode errorCode) {
        return new ResultDataResponseDTO<>(null, errorCode);
    }
}


