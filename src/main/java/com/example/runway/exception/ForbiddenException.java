package com.example.runway.exception;

import com.example.runway.common.CommonResponseStatus;
import lombok.Getter;


import static com.example.runway.common.CommonResponseStatus._BAD_REQUEST;

@Getter
public class ForbiddenException extends BaseException {
    private String message;

    public ForbiddenException(String message) {
        super(_BAD_REQUEST);
        this.message = message;
    }

    public ForbiddenException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public ForbiddenException(CommonResponseStatus errorCode) {
        super(errorCode);
    }

}
