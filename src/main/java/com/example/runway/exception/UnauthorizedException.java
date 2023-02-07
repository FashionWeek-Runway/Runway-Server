package com.example.runway.exception;

import lombok.Getter;
import com.example.runway.constants.CommonResponseStatus;

import static com.example.runway.constants.CommonResponseStatus._UNAUTHORIZED;

@Getter
public class UnauthorizedException extends BaseException {
    private String message;

    public UnauthorizedException(String message) {
        super(_UNAUTHORIZED);
        this.message = message;
    }

    public UnauthorizedException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public UnauthorizedException(CommonResponseStatus errorCode) {
        super(errorCode);
    }

}
