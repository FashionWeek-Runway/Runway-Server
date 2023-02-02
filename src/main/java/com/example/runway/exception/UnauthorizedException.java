package com.example.runway.exception;

import com.example.runway.exception.BaseException;
import lombok.Getter;
import com.example.runway.common.CommonResponseStatus;

import static com.example.runway.common.CommonResponseStatus.UNAUTHORIZED;

@Getter
public class UnauthorizedException extends BaseException {
    private String message;

    public UnauthorizedException(String message) {
        super(UNAUTHORIZED);
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
