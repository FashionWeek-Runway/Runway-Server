package com.example.runway.convertor;

import com.example.runway.domain.Authority;
import com.example.runway.domain.User;
import com.example.runway.domain.UserCategory;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class UserConvertor {
    public static Authority PostAuthroity() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public static User SignUpUser(UserReq.SignupUser signupUser, Authority authority, String passwordEncoded, String profileImgUrl) {
        return User.builder()
                .profileImgUrl(profileImgUrl)
                .password(passwordEncoded)
                .name(signupUser.getName())
                .nickname(signupUser.getNickname())
                .username(signupUser.getPhone())
                .gender(signupUser.getGender())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .agreeInfo(true)
                .alarm(true)
                .loginDate(LocalDateTime.now())
                .build();
    }


    public static UserCategory PostUserCategory(Long userId, Long categoryId) {
        return UserCategory.builder().categoryId(categoryId).userId(userId).build();
    }


    public static UserRes.SignUp SignUpUserRes(UserRes.GenerateToken token, User user, List<String> categoryList) {
        return UserRes.SignUp.builder().userId(user.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .imgUrl(user.getProfileImgUrl())
                .nickname(user.getNickname())
                .categoryList(categoryList)
                .build();
    }

    public static User SignUpSocialUser(UserReq.SocialSignUp socialSignUp, Authority authority, String passwordEncoded, String profileImgUrl) {
        return User.builder()
                .profileImgUrl(profileImgUrl)
                .password(passwordEncoded)
                .name(socialSignUp.getNickname())
                .nickname(socialSignUp.getNickname())
                .username(socialSignUp.getSocialId())
                .social(socialSignUp.getType())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .agreeInfo(true)
                .alarm(true)
                .loginDate(LocalDateTime.now())
                .build();
    }

}
