package com.example.runway.convertor;

import com.example.runway.domain.ReviewReport;
import com.example.runway.domain.StoreReview;
import com.example.runway.dto.store.ReviewReq;

public class ReviewConvertor {
    public static StoreReview UploadImg(Long storeId, Long userId, String imgUrl) {
        return StoreReview.builder().storeId(storeId).userId(userId).imgUrl(imgUrl).build();
    }

    public static ReviewReport ReportReview(Long userId, ReviewReq.ReportReview reportReview) {
        return ReviewReport.builder()
                .userId(userId)
                .reviewId(reportReview.getReviewId())
                .reasonId(reportReview.getReason())
                .opinion(reportReview.getOpinion())
                .build();
    }
}
