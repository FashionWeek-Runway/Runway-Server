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
}
