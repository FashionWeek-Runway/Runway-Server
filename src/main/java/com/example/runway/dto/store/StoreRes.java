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
    public static class getHomeList{
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
        private String store;
        @ApiModelProperty(notes ="지역 정보가 나옵니다.", required = true, example = "무신사 스탠다드 성수")
        private String region;
    }
}
