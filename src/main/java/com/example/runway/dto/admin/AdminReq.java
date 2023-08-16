package com.example.runway.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
}
