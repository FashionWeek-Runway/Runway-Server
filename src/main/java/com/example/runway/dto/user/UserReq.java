package com.example.runway.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "👤 유저 로그인 API Request")
    public static class LoginUserInfo {
        @Schema(description = "로그인 할 전화번호", required = true, example = "01012345678")
        private String phone;
        @Schema(description = "비밀번호", required = true, example = "runway8925!")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "👤 유저 회원가입 API Request")
    public static class SignupUser {
        @Schema(description = "전화번호", required = true, example = "01012345678")
        private String phone;

        @Schema(description = "실명", required = true, example = "임현우")
        private String name;

        @Schema(description = "비밀번호", required = true, example = "runway8925!")
        private String password;

        @Schema(description = "닉네임", required = true, example = "이메누")
        private String nickname;

        @Schema(description = "성별", required = true, example = "남자 or 여자")
        private String gender;
    }
}
