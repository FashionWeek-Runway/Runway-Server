package com.example.runway.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "👤 유저 로그인 or 회원가입 API Response")
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
    @ApiModel(value = "👤 유저 로그인 or 회원가입 API Response")
    public static class ReIssueToken {
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
    }

}
