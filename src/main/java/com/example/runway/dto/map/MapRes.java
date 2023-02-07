package com.example.runway.dto.map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


public class MapRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-01 메인 지도 조회 + 필터링 조회 🗺 API Response")
    public static class GetMapRes {

        @ApiModelProperty(notes = "쇼룸 id", required = true, example = "1")
        private Long storeId;

        @ApiModelProperty(notes = "쇼룸 이름", required = true, example = "무신사 스탠다드")
        private String storeName;

        @ApiModelProperty(notes = "위도", required = true, example = "37.544499")
        private double latitude;

        @ApiModelProperty(notes = "경도", required = true, example = "127.055327")
        private double longitude;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-02 지도 필터링 조회 🗺 API Response")
    public static class GetStoreInfoListRes {

        @ApiModelProperty(notes = "쇼룸 id", required = true, example = "1")
        private Long storeId;
        
        @ApiModelProperty(notes = "쇼룸 썸네일 이미지",required = true,example = "이미지 url")
        private String storeImg;

        @ApiModelProperty(notes = "쇼룸 카테고리 리스트",required = true,example = "[\"스트릿\",\"미니멀\"]")
        private List<String> storeCategory;

        @ApiModelProperty(notes = "쇼룸 이름", required = true, example = "무신사 스탠다드")
        private String storeName;


    }
}
