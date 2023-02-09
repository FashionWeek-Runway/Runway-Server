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
    @ApiModel(value = "03-02 쇼룸 상단정보  🏬 API Response")
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
    @ApiModel(value = "03-03 쇼룸 사용자 후기 🏬 API Response")
    public static class StoreReview {
        private Long reviewId;
        private String imgUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-04 쇼룸 블로그 크롤링 🏬 API Response")
    public static class StoreBlog {
        private String webUrl;
        private String imgUrl;
        private String title;
    }
}
