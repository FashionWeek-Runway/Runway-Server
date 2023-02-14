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
    @ApiModel(value = "04-02 지도 필터링 조회 🗺 API Response")
    public static class FilterMap {
        @ApiModelProperty(notes = "카테고리 리스트",required = true,example = "[\"스트릿\",\"미니멀\"]")
        private List<String> category;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-03,04 지도 쇼룸 검색 조회 🗺 API Response")
    public static class SearchStore {
        @ApiModelProperty(notes = "지도 중심의 위도", required = true, example = "37.544499")
        private double latitude;

        @ApiModelProperty(notes = "지도 중심의 경도", required = true, example = "127.055327")
        private double longitude;
    }
}
