package com.example.imenu_spring_project.dto;

import lombok.*;

public class UserReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserInfo {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupUser {
        private String username;

        private String password;

        private String nickname;

        private String name;

        private String phone;
    }
}
