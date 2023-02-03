package com.example.runway.convertor;

import com.example.runway.domain.Authority;
import com.example.runway.domain.User;
import com.example.runway.domain.UserCategory;
import com.example.runway.dto.user.UserReq;

import java.time.LocalDateTime;
import java.util.Collections;

public class UserConvertor {
    public static Authority PostAuthroity() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public static User SignUpUser(UserReq.SignupUser signupUser, Authority authority, String passwordEncoded) {
        return User.builder()
                .password(passwordEncoded)
                .name(signupUser.getName())
                .nickname(signupUser.getNickname())
                .username(signupUser.getPhone())
                .gender(signupUser.getGender())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .loginDate(LocalDateTime.now())
                .build();
    }


    public static UserCategory PostUserCategory(Long userId, Long categoryId) {
        return UserCategory.builder().categoryId(categoryId).userId(userId).build();
    }


}
