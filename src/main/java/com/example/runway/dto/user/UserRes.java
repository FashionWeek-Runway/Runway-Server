package com.example.runway.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-02 유저 로그인 🔑 API Response")
    public static class Token {
        @ApiModelProperty(notes = "user 인덱스", required = true, example = "1")
        private Long userId; //user 인덱스
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
        @Schema(description = "리프레쉬 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ----")
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class GenerateToken{
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-01 토큰 재발급 🔑 API Response")
    public static class ReIssueToken {
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-01,10 회원가입 🔑 API Response")
    public static class SignUp {
        @ApiModelProperty(notes = "user 인덱스", required = true, example = "1")
        private Long userId; //user 인덱스
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
        @Schema(description = "리프레쉬 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ----")
        private String refreshToken;
        @ApiModelProperty(notes = "프로필 이미지",required = true,example = "이미지 url")
        private String imgUrl;
        @ApiModelProperty(notes = "유저 닉네임",required = true,example = "이미지 url")
        private String nickname;
        @ApiModelProperty(notes = "카테고리 리스트",required = true,example = "카테고리 리스트")
        private List<String> categoryList;


    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-07 카카오 로그인 🔑 API Response")
    public static class SocialSignUp {
        @ApiModelProperty(notes = "소셜 id", required = true, example = "214124215125")
        private String id;

        @ApiModelProperty(notes = "소셜 프로필 사진", required = true, example = "이미지 url")
        private String profileImgUrl;
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-06 문자인증 🔑 API Response")
    public static class SmsResponse{
        @ApiModelProperty(notes = "요청 id", required = true, example = "VORSSA-21j4nl12n4ln12kl4n~~~")
        private String requestId;
        @ApiModelProperty(notes = "요청 시간", required = true, example = "2023-02-07T23:02:21.275")
        private LocalDateTime requestTime;
        @ApiModelProperty(notes = "요청 상태 코드", required = true, example = "200")
        private String statusCode;
        @ApiModelProperty(notes = "요청 상태", required = true, example = "true")
        private String statusName;
        @ApiModelProperty(notes = "문자인증 난수 6글자입니다. 해당 반환값으로 확인하시면 됩니다", required = true, example = "123456")
        private String smsConfirmNum;


    }


}
