package com.example.runway.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-02 유저 로그인 API Request 🔑")
    public static class LoginUserInfo {
        @ApiModelProperty(notes = "로그인 할 전화번호", example = "01012345678")
        private String phone;
        @ApiModelProperty(notes ="비밀번호", required = true, example = "runway8925!")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-01 유저 회원가입 API Request 🔑")
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

        @ApiModelProperty(notes="프로필 파일 업로드",required = true,example = "사진 파일 업로드")
        private MultipartFile multipartFile;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-05 유저 비밀번호 재설정 API Request🔑")
    public static class PostPassword {
        @ApiModelProperty(notes ="유저의 전화번호", required = true, example = "01012345678")
        private String phone;

        @ApiModelProperty(notes ="변경할 비밀번호", required = true, example = "runway8925!")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-06,07 소셜 로그인 토큰 API Request🔑")
    public static class SocialReq {
        @ApiModelProperty(notes ="액세스 토큰", required = true, example = "소셜 액세스 토큰")
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-09 소셜 회원가입 Request🔑")
    public static class SocialSignUp {
        @ApiModelProperty(notes = "소셜 id", required = true, example = "214124215125")
        private String socialId;

        @ApiModelProperty(notes = "소셜 프로필 사진", required = true, example = "이미지 url")
        private String profileImgUrl;

        @ApiModelProperty(notes = "닉네임", required = true, example = "이메누")
        private String nickname;

        @ApiModelProperty(notes = "ArrayList<Long> 형식입니다. 취향", required = true, example = "[1,2,3,4]")
        private List<Long> categoryList;

        @ApiModelProperty(notes="프로필 사진 변경",required = true,example = "프로필 사진 변경")
        private MultipartFile multipartFile;
    }
}
