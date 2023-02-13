package com.example.runway.convertor;

import com.example.runway.domain.StoreReview;

public class ReviewConvertor {
    public static StoreReview UploadImg(Long storeId, Long userId, String imgUrl) {
        return StoreReview.builder().storeId(storeId).userId(userId).imgUrl(imgUrl).build();
    }
}
