package com.example.runway.dto.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class HomeReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "05-02 홈화면 카테고리 선택 🏠 API Request")
    public static class PostUserCategory {
        @ApiModelProperty(notes = "ArrayList<Long> 형식입니다. 취향", required = true, example = "[1,2,3,4]")
        private List<Long> categoryList;
    }
}
