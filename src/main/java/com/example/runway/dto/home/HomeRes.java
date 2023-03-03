package com.example.runway.dto.home;

import com.example.runway.dto.user.UserRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class HomeRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "05-03 홈화면 쇼룸 조회 🏠 API Response")
    public static class StoreInfo {
        @ApiModelProperty(notes="쇼룸 북마크 유무",required = true,example = "true")
        private boolean bookmark;
        @ApiModelProperty(notes ="이미지 url", required = true, example = "이미지 url~~")
        private String imgUrl;
        @ApiModelProperty(notes="쇼룸 ID", required = true,example = "1")
        private Long storeId;
        @ApiModelProperty(notes ="쇼룸 이름", required = true, example = "무신사 스탠다드 성수")
        private String storeName;
        @ApiModelProperty(notes="쇼룸 지역정보",required = true,example = "성수, 서울")
        private String regionInfo;
        @ApiModelProperty(notes = "쇼룸 카테고리 리스트 ",required = true,example = "[\"스트릿\",\"미니멀\"]")
        private List<String> categoryList;
        @ApiModelProperty(notes = "북마크 갯수",required = true,example = "2")
        private int bookmarkCnt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "05-04 홈화면 리뷰 조회 🏠 API Response")
    public static class Review {
        @ApiModelProperty(notes="reviewId",required = true,example = "1")
        private Long reviewId;
        @ApiModelProperty(notes="imgUrl",required = true,example = "리뷰 이미지")
        private String imgUrl;
        @ApiModelProperty(notes="리뷰 지역정보",required = true,example = "성수, 서울")
        private String regionInfo;
        @ApiModelProperty(notes="리뷰 읽음 유무",required = true,example = "true")
        private boolean isRead;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "05-05 홈화면 리뷰 상세 조회 🏠 API Response")
    public static class ReviewInfo {
        @ApiModelProperty(notes="reviewId",required = true,example = "1")
        private Long reviewId;
        @ApiModelProperty(notes = "프로필 이미지",required = false,example = "이미지 url")
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
        private UserRes.ReviewInquiry reviewInquiry;
    }
}
