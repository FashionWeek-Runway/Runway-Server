package com.example.runway.dto.user;

import com.example.runway.dto.store.StoreRes;
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-09 유저 로그인 👤 API Response")
    public static class AppleLogin {
        @ApiModelProperty(notes = "애플 유저 회원가입 유무", required = true, example = "true")
        private boolean checkUser;
        @ApiModelProperty(notes = "appleId", required = true, example = "rfqwlkmlkdn2qlnrjkn")
        private String appleId;
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
    @ApiModel(value = "02-01 토큰 재발급 👤 API Response")
    public static class ReIssueToken {
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-01,10 회원가입 👤 API Response")
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

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-06 내가 작성한 리뷰 보기 👤 API Response")
    public static class Review {
        @ApiModelProperty(notes="reviewId",required = true,example = "1")
        private Long reviewId;
        @ApiModelProperty(notes="imgUrl",required = true,example = "리뷰 이미지")
        private String imgUrl;
        @ApiModelProperty(notes="쇼룸 지역정보",required = true,example = "성수, 서울")
        private String regionInfo;
    }
    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-06 리뷰 상세 👤 API Response")
    public static class ReviewDetail {
        @ApiModelProperty(notes="reviewId",required = true,example = "1")
        private Long reviewId;
        @ApiModelProperty(notes="imgUrl",required = true,example = "리뷰 이미지")
        private String imgUrl;
        @ApiModelProperty(notes="쇼룸 지역정보",required = true,example = "성수, 서울")
        private String regionInfo;
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-03 마이페이지 조회(사장님 여부까지 포함) 👤 API Response")
    public static class UserInfo {
        @ApiModelProperty(notes = "프로필 이미지",required = true,example = "이미지 url")
        private String imgUrl;
        @ApiModelProperty(notes = "유저 닉네임",required = true,example = "이미지 url")
        private String nickname;
        @ApiModelProperty(notes="유저 사장님 유무",required = true,example = "true")
        private boolean ownerCheck;
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-04 프로필 편집을 위한 기존 데이터 GET 👤 API Response")
    public static class PatchUserInfo {
        @ApiModelProperty(notes = "프로필 이미지",required = true,example = "이미지 url")
        private String imgUrl;
        @ApiModelProperty(notes = "유저 닉네임",required = true,example = "이미지 url")
        private String nickname;
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-08 쇼룸 북마크 리스트 👤 API Response")
    public static class StoreInfo {
        @ApiModelProperty(notes = "쇼룸 id", required = true, example = "1")
        private Long storeId;

        @ApiModelProperty(notes = "쇼룸 썸네일 이미지",required = true,example = "이미지 url")
        private String storeImg;


        @ApiModelProperty(notes = "쇼룸 카테고리 리스트 ",required = true,example = "[\"스트릿\",\"미니멀\"]")
        private List<String> category;

        @ApiModelProperty(notes = "쇼룸 이름", required = true, example = "무신사 스탠다드")
        private String storeName;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-07 내 리뷰 조회 🏬 API Response")
    public static class ReviewInfo {
        @ApiModelProperty(notes="reviewId",required = true,example = "1")
        private Long reviewId;
        @ApiModelProperty(notes = "프로필 이미지",required = true,example = "이미지 url")
        private String profileImgUrl;
        @ApiModelProperty(notes = "닉네임",required = true,example = "이름")
        private String nickname;
        @ApiModelProperty(notes="imgUrl",required = true,example = "리뷰 이미지")
        private String imgUrl;
        @ApiModelProperty(notes="쇼룸 Id",required = true,example = "1")
        private Long storeId;
        @ApiModelProperty(notes = "쇼룸 이름",required = true,example = "노드 아카이브")
        private String storeName;
        @ApiModelProperty(notes="쇼룸 지역정보",required = true,example = "성수, 서울")
        private String regionInfo;
        @ApiModelProperty(notes = "리뷰 북마크 갯수",required = true,example = "false")
        private int bookmarkCnt;
        @ApiModelProperty(notes = "리뷰 내 게시글 유무",required = true,example = "false")
        private boolean isMy;
        @ApiModelProperty(notes="쇼룸 리뷰 이전 id, 다음 id",example = "이전 id, 다음 id")
        private ReviewInquiry reviewInquiry;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-07 리뷰 이전 id, 다음 id 🏬 API Response")
    public static class ReviewInquiry{
        @ApiModelProperty(notes="이전 리뷰 ID",required = true,example = "1")
        private Long prevReviewId;
        @ApiModelProperty(notes="다음 리뷰 ID",required = true,example = "1")
        private Long nextReviewId;
    }


}
