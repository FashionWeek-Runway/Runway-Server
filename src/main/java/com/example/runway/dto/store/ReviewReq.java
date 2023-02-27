package com.example.runway.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

public class ReviewReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-10 리뷰 신고🏬 API Request FRAME REPORT")
    public static class ReportReview{
        @ApiModelProperty(notes="reviewId",required = true,example = "1")
        private Long reviewId;
        @ApiModelProperty(notes = "신고 사유",required = true,example = "")
        private int reason;
        private String opinion;
    }
}
