package com.example.runway.exception;

import com.example.runway.constants.CommonResponseStatus;
import lombok.Getter;

import static com.example.runway.constants.CommonResponseStatus._BAD_REQUEST;

@Getter
public class BadRequestException extends BaseException {
    private String message;

    public BadRequestException(String message) {
        super(_BAD_REQUEST);
        this.message = message;
    }

    public BadRequestException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public BadRequestException(CommonResponseStatus errorCode) {
        super(errorCode);
    }
}
