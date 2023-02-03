package com.example.runway.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class UserReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "👤 유저 로그인 API Request")
    public static class LoginUserInfo {
        @ApiModelProperty(notes = "로그인 할 전화번호", example = "01012345678")
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
        @ApiModelProperty(notes ="전화번호", required = true, example = "01012345678")
        private String phone;

        @ApiModelProperty(notes = "실명", required = true, example = "임현우")
        private String name;

        @ApiModelProperty(notes ="비밀번호", required = true, example = "runway8925!")
        private String password;

        @ApiModelProperty(notes = "닉네임", required = true, example = "이메누")
        private String nickname;

        @ApiModelProperty(notes = "성별", required = true, example = "남자 or 여자")
        private String gender;

        @ApiModelProperty(notes = "ArrayList<Long> 형식입니다. 취향", required = true, example = "[1,2,3,4]")
        private List<Long> categoryList;

    }
}
