package com.example.runway.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class AdminReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostFeed{
        @Schema(description = "인스타 링크", required = true, example = "tbt")
        private String link;
        @Schema(description = "가게 이름", required = true, example = "성수")
        private String storeName;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreInfo {
        private String storeName;

        private String website;

        private String phoneNumber;

        private String time;

        private String address;

        private String instagramLink;

        private double latitude;

        private double longitude;

        private int region;

        private List<Long> categoryList;
    }
}
