package com.example.runway.dto.map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class MapReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-02 지도 필터링 조회 🗺 API Request FRAME MAP_03,04")
    public static class FilterMap {
        @ApiModelProperty(notes = "카테고리 리스트",required = true,example = "[\"스트릿\",\"미니멀\"]")
        private List<String> category;

        @ApiModelProperty(notes = "유저의 위도", required = true, example = "37.56653588195168")
        private double latitude;

        @ApiModelProperty(notes = "유저의 경도", required = true, example = "126.97864102209026")
        private double longitude;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-03,05 지도 쇼룸 검색 조회 🗺 API Request FRAME SEARCH_07")
    public static class SearchStore {
        @ApiModelProperty(notes = "검색어", required = true, example = "성수")
        private String content;

        @ApiModelProperty(notes = "지도 중심의 위도", required = true, example = "37.56653588195168")
        private double latitude;

        @ApiModelProperty(notes = "지도 중심의 경도", required = true, example = "126.97864102209026")
        private double longitude;
    }
}
