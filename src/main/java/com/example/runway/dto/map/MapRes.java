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
    @ApiModel(value = "04-01 메인 지도 조회 + 필터링 조회 마커용 🗺 API Response FRAME MAP_03,04")
    public static class Map {

        @ApiModelProperty(notes = "쇼룸 id", required = true, example = "1")
        private Long storeId;

        @ApiModelProperty(notes = "쇼룸 이름", required = true, example = "무신사 스탠다드")
        private String storeName;

        @ApiModelProperty(notes = "쇼룸 북마크 유무",required = true,example = "미니멀")
        private boolean bookmark;

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
    @ApiModel(value = "04-02,04,06 하단 스와이프 쇼룸 필터링 조회 🗺 API Response FRAME MAP_03,04, MAP_07, SEARCH_02")
    public static class StoreInfo {

        @ApiModelProperty(notes = "쇼룸 id", required = true, example = "1")
        private Long storeId;
        
        @ApiModelProperty(notes = "쇼룸 썸네일 이미지",required = true,example = "이미지 url")
        private String storeImg;


        @ApiModelProperty(notes = "쇼룸 카테고리 리스트 ",required = true,example = "[\"스트릿\",\"미니멀\"]")
        private List<String> category;

        @ApiModelProperty(notes = "쇼룸 이름", required = true, example = "무신사 스탠다드")
        private String storeName;


        private boolean bookmark;

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
    @ApiModel(value = "04-03 지도 쇼룸 검색 조회 검색용 🗺 API Response FRAME SEARCH_07")
    public static class SearchList {
        private List<RegionSearchList> regionSearchList;
        private List<StoreSearchList> storeSearchList;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-03-01 지도 지역 정보 검색 조회용 🗺 API Response")
    public static class RegionSearchList{

        @ApiModelProperty(notes="지역 ID", required = true,example = "1")
        private Long regionId;

        @ApiModelProperty(notes="지역이름", required = true,example = "성수동")
        private String region;

        @ApiModelProperty(notes="주소",required = true,example = "서울특별시 성동구 성수동1가")
        private String address;

    }




    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-03-02 지도 쇼룸 정보 검색 조회용 🗺 API Response")
    public static class StoreSearchList {

        @ApiModelProperty(notes="쇼룸 ID", required = true,example = "1")
        private Long storeId;

        @ApiModelProperty(notes ="쇼룸 이름", required = true, example = "무신사 스탠다드 성수")
        private String storeName;

        @ApiModelProperty(notes="쇼룸 주소",required = true,example = "서울시 성동구 ~~")
        private String address;




    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-03-02 지도 쇼룸 정보 검색 조회용 🗺 API Response FRAME SEARCH_01")
    public static class MapMarkerList {

        @ApiModelProperty(notes="쇼룸 ID", required = true,example = "1")
        private Long storeId;

        @ApiModelProperty(notes ="쇼룸 이름", required = true, example = "무신사 스탠다드 성수")
        private String storeName;

        @ApiModelProperty(notes="쇼룸 주소",required = true,example = "서울시 성동구 ~~")
        private String address;

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
    @ApiModel(value = "04-07 지도 쇼룸 검색 조회용 🗺 API Response FRAME SEARCH_03,04")
    public static class StorePositionAndInfo {
        @ApiModelProperty(notes="쇼룸 마커용", required = true,example = "")
        private MapMarkerList mapMarker;
        @ApiModelProperty(notes="쇼룸 하단 스와이프 조회", required = true,example = "")
        private StoreInfo storeInfo;


    }
}
