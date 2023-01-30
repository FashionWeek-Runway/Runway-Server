package com.example.imenu_spring_project.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum JwtErrorCode {
    UsernameOrPasswordNotFoundException (false,400, "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ForbiddenException(false,403, "해당 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZEDException (false,401, "로그인 후 이용가능합니다.", HttpStatus.UNAUTHORIZED),
    ExpiredJwtException(false,444, "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요.", HttpStatus.UNAUTHORIZED),
    ReLogin(false,445, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.UNAUTHORIZED),
    InvalidToken(false,446,"토큰이 올바르지 않습니다." ,HttpStatus.UNAUTHORIZED ),
    HijackJwtToken(false,407,"탈취된(로그아웃 된) 토큰입니다 다시 로그인해주세요.",HttpStatus.UNAUTHORIZED);


    @Getter
    private boolean isSuccess;

    @Getter
    private int code;

    @Getter
    private String message;

    @Getter
    private HttpStatus status;

    JwtErrorCode(boolean isSuccess,int code, String message, HttpStatus status) {
        this.isSuccess=isSuccess;
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
