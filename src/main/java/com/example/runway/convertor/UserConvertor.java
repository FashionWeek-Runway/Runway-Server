package com.example.runway.convertor;

import com.example.runway.domain.Authority;
import com.example.runway.domain.Social;
import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.repository.StoreReviewRepository;

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
                .status(true)
                .loginDate(LocalDateTime.now())
                .build();
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
                .status(true)
                .loginDate(LocalDateTime.now())
                .build();
    }

    public static UserRes.UserInfo UserInfo(User user) {
        return UserRes.UserInfo.builder()
                .imgUrl(user.getProfileImgUrl())
                .nickname(user.getNickname())
                .ownerCheck(user.isOwner())
                .build();
    }

    public static UserRes.PatchUserInfo UserProfile(User user) {
        return UserRes.PatchUserInfo.builder()
                .imgUrl(user.getProfileImgUrl())
                .nickname(user.getNickname())
                .build();
    }

    public static UserRes.ReviewInfo MyReviewDetail(StoreReviewRepository.GetStoreReview result, UserRes.ReviewInquiry reviewInquiry,Long userId) {
        return UserRes.ReviewInfo.builder().reviewId(result.getReviewId())
                .profileImgUrl(result.getProfileImgUrl())
                .nickname(result.getNickname())
                .imgUrl(result.getImgUrl())
                .storeId(result.getStoreId())
                .storeName(result.getStoreName())
                .regionInfo(result.getRegionInfo())
                .bookmarkCnt(result.getBookmarkCnt())
                .reviewInquiry(reviewInquiry)
                .isMy(userId.equals(result.getUserId()))
                .build();
    }

    public static UserRes.SettingInfo SettingInfo(boolean kakao, boolean apple, String username, boolean social) {
        return UserRes.SettingInfo.builder().social(social).apple(apple).kakao(kakao).phone(username).build();
    }


    public static Social SyncSocial(String socialId, Long userId, String social) {
        return Social.builder().socialId(socialId).userId(userId).type(social).build();
    }
}
