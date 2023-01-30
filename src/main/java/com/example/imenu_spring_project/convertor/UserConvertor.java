package com.example.imenu_spring_project.convertor;

import com.example.imenu_spring_project.domain.Authority;
import com.example.imenu_spring_project.domain.User;
import com.example.imenu_spring_project.dto.UserReq;

import java.util.Collections;

public class UserConvertor {
    public static Authority PostAuthroity() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public static User SignUpUser(UserReq.SignupUser signupUser, Authority authority, String passwordEncoded) {
        return User.builder()
                .username(signupUser.getUsername())
                .password(passwordEncoded)
                .name(signupUser.getName())
                .nickname(signupUser.getNickname())
                .phone(signupUser.getPhone())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();
    }
}
