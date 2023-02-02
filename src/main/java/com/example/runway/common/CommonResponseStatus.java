package com.example.runway.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 에러 코드 관리
 */
@Getter
@AllArgsConstructor
public enum CommonResponseStatus {

    /**
     * 잘못된 요청
     */
    INTERNAL_SERVER_ERROR(false, "COMMON000", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(false,"COMMON000","잘못된 요청입니다."),
    UNAUTHORIZED(false,"COMMON001","권한이 잘못되었습니다"),

    /*
        인증 관련 에러코드
     */
    ForbiddenException(false,"AUTH002", "해당 요청에 대한 권한이 없습니다."),
    UNAUTHORIZED_EXCEPTION (false,"AUTH003", "로그인 후 이용가능합니다. 토큰을 입력해 주세요"),
    EXPIRED_JWT_EXCEPTION(false,"AUTH004", "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    RELOGIN_EXCEPTION(false,"AUTH005", "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_TOKEN_EXCEPTION(false,"AUTH006","토큰이 올바르지 않습니다." ),
    HIJACK_JWT_TOKEN_EXCEPTION(false,"AUTH007","탈취된(로그아웃 된) 토큰입니다 다시 로그인해주세요."),
    NOT_EXIST_USER(false, "AUTH008", "존재하지 않는 유저입니다."),


    /**


     * 1000 : 요청 성공
     */
    SUCCESS(true, "1000", "요청에 성공하였습니다."),

    /*
        토큰 관련 예외 코드
     */



    /**
     * UXXX : USER 관련 에러
     */
    //




    INVALID_USER_JWT(false,"U001","권한이 없는 유저의 접근입니다."),
    INVALID_REFRESH_TOKEN(false,"U001" ,"리프레쉬 토큰이 만료되었습니다 다시 로그인해주세요." ),
    HIJACK_ACCESS_TOKEN(false,"U001","탈취된(로그아웃 된) 토큰입니다 다시 로그인해주세요."),

    USERS_EMPTY_USER_ID(false, "U001", "유저 아이디 값을 입력해주세요."),
    USERS_EMPTY_USER_PASSWORD(false, "U001", "유저 비밀번호를 입력해주세요."),


    POST_USERS_EMPTY_EMAIL(false, "U001", "이메일을 입력해주세요."),

    POST_USERS_INVALID_EMAIL(false, "U001", "이메일 형식을 확인해주세요."),

    POST_USERS_EXISTS_EMAIL(false,"U001","중복된 이메일입니다."),

    TOO_SHORT_PASSWORD(false, "U001", "비밀번호의 길이를 8자 이상을 설정해주세요."),

    FAILED_TO_SIGN_UP(false, "U001", "회원가입에 실패하였습니다."),

    USERS_EXISTS_ID(false,"U001","중복된 전화번호입니다."),

    USERS_EXISTS_NICKNAME(false,"U001","중복된 닉네임입니다."),

    POST_USERS_EMPTY_NICKNAME(false,"U001","닉네임을 입력해주세요."),

    FAILED_TO_LOGIN(false, "U001", "로그인에 실패하였습니다."),


    NOT_EXIST_NICKNAME(false, "U001", "존재하지 않는 닉네임입니다."),

    NOT_EXIST_EMAIL(false, "U001", "존재하지 않는 이메일입니다."),

    NOT_CORRECT_PASSWORD(false, "U001", "비밀번호가 일치하지 않습니다."),

    ALREADY_DELETED_USER(false, "U001", "이미 탈퇴된 유저입니다."),
    NOT_CORRECT_USER(false,"U001","회원 정보에 일치하는 아이디가 없습니다."),
    NOT_EXIST_KAKAO(false,"U001","회원정보에 존재하는 카카오가 존재하지 않습니다. 회원가입 해주세요"),


    NOT_EXIST_MYFOLIO_ID(false, "U001", "존재하지 않는 MyFolio입니다"),

    NOT_POST_CONTENT(false,"U001","내용을 입력해주세요."),

    NOT_CORRECT_PHONE_NUMBER_FORM(false, "U001", "전화번호를 하이픈(-) 없이 입력해주세요"),

    NOT_CORRECT_PASSWORD_FORM(false, "U001", "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.");

    //Planning






















    private final boolean isSuccess;
    private final String code;
    private final String message;


}
