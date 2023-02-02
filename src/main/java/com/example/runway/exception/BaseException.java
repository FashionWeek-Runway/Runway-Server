package com.example.runway.exception;

import com.example.runway.common.CommonResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    private CommonResponseStatus status;
}
