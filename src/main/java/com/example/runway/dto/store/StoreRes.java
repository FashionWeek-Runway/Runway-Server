package com.example.runway.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class StoreRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03. 쇼룸🏬 API Response")
    public static class HomeList{
        @ApiModelProperty(notes ="추천 쇼롬", required = true, example = "추천 쇼룸입니다.")
        private StorePreview recommendStore;
        @ApiModelProperty(notes ="쇼룸", required = true, example = "쇼룸 리스트가 나옵니다.")
        private List<StorePreview> storeList;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StorePreview{
        @ApiModelProperty(notes ="쇼룸 Id", required = true, example = "1")
        private Long storeId;
        @ApiModelProperty(notes ="이미지 url", required = true, example = "이미지 url~~")
        private String imgUrl;
        @ApiModelProperty(notes ="카테고리 리스트가 나옵니다.", required = true, example = "[\"미니멀\",\"페미닌\"]")
        private List<String> category;
        @ApiModelProperty(notes ="쇼룸 이름", required = true, example = "무신사 스탠다드 성수")
        private String storeName;
        @ApiModelProperty(notes ="지역 정보가 나옵니다.", required = true, example = "무신사 스탠다드 성수")
        private String region;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-02 쇼룸 상단정보 🏬 API Response FRAME MAPDETAIL_01")
    public static class StoreInfo {
        @ApiModelProperty(notes ="쇼룸 Id", required = true, example = "1")
        private Long storeId;

        @ApiModelProperty(notes ="이미지 url 리스트", required = true, example = "[\"이미지url\",\"이미지url2\"]")
        private List<String> imgUrlList;

        @ApiModelProperty(notes ="카테고리 리스트가 나옵니다.", required = true, example = "[\"미니멀\",\"페미닌\"]")
        private List<String> category;

        @ApiModelProperty(notes ="쇼룸 이름", required = true, example = "무신사 스탠다드 성수")
        private String storeName;

        @ApiModelProperty(notes="쇼룸 주소",required = true,example = "서울시 성동구 ~~")
        private String address;

        @ApiModelProperty(notes="쇼룸 운영시간",required = true,example = "월 - 일 08:00 ~ 21:00")
        private String storeTime;

        @ApiModelProperty(notes="쇼룸 전화번호", required = true,example = "01012445678")
        private String storePhone;

        @ApiModelProperty(notes="인스타 그램 링크",required = true,example = "해당 매장의 인스타그램 링크")
        private String instagram;

        @ApiModelProperty(notes="웹사이트 링크",required = true,example = "해당 매장의 웹사이트 링크")
        private String webSite;

        @ApiModelProperty(notes = "해당 매장 유저의 북마크 여부",required = true,example = "true")
        private boolean bookmark;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-03 쇼룸 사용자 후기 🏬 API Response FRAME MAPDETAIL_01")
    public static class StoreReview {
        private Long reviewId;
        private String imgUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-04 쇼룸 블로그 스크랩핑 🏬 API Response FRAME MAPDETAIL_01")
    public static class StoreBlog {
        @ApiModelProperty(notes="네이버 view 링크",required = true,example = "네이버 view 링크")
        private String webUrl;
        @ApiModelProperty(notes="네이버 view imgUrl",required = true,example = "네이버 view imgUrl")
        private String imgUrl;
        @ApiModelProperty(notes="네이버 view img 갯수",required = true,example = "네이버 view img 갯수")
        private int imgCnt;
        @ApiModelProperty(notes="네이버 view 제목",required = true,example = "네이버 view 제목")
        private String title;
        @ApiModelProperty(notes="네이버 view 내용",required = true,example = "네이버 view 내용")
        private String content;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-06 쇼룸 리뷰 조회 🏬 API Response")
    public static class ReviewInfo {
        private Long reviewId;
        private String imgUrl;
        private String address;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-06 쇼룸 사장님 소식 조회 리스트🏬 API Response FRAME MAPDETAIL_01")
    public static class StoreBoardList {
        @ApiModelProperty(notes="imgUrl",required = true,example = "소식 대표이미지")
        private String imgUrl;
        @ApiModelProperty(notes="소식 Id",required = true,example = "1")
        private Long feedId;
        @ApiModelProperty(notes="소식 제목",required = true,example = "소식 제목")
        private String title;
        @ApiModelProperty(notes="소식 게시 날짜",required = true,example = "MM.DD")
        private String day;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-07 쇼룸 사장님 소식 조회🏬  API Response FRAME feed_01")
    public static class StoreBoard {
        @ApiModelProperty(notes="소식 게시글 보유자 유무",required = true,example = "true")
        private boolean myBoard;
        @ApiModelProperty(notes="소식 게시글 북마크 유무",required = true,example = "true")
        private boolean bookmark;
        @ApiModelProperty(notes="imgUrl 리스트가 나옵니다",required = true,example = "[\"imgUrl\",\"imgUrl\",\"imgUrl\"]")
        private List<String> imgUrl;
        @ApiModelProperty(notes="소식 Id",required = true,example = "1")
        private Long feedId;
        @ApiModelProperty(notes="소식 제목",required = true,example = "소식 제목")
        private String title;
        @ApiModelProperty(notes="소식 게시 날짜",required = true,example = "YYYY.MM.DD HH:MM")
        private String day;
        @ApiModelProperty(notes="소식 내용",required = true,example = "소식 내용")
        private String content;
        @ApiModelProperty(notes="쇼룸 Id",required = true,example = "1")
        private Long storeId;
        @ApiModelProperty(notes = "쇼룸 이름",required = true,example = "노드 아카이브")
        private String storeName;
        @ApiModelProperty(notes="쇼룸 지역정보",required = true,example = "성수, 서울")
        private String regionInfo;
    }

}
