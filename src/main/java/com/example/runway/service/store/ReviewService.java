package com.example.runway.service.store;

import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.ReviewReq;
import com.example.runway.dto.store.StoreRes;

import java.io.IOException;
import java.util.List;

public interface ReviewService {

    void postStoreReview(Long storeId, Long userId, byte[] multipartFile) throws IOException;

    PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page, int size);

    StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId, Long userId);

    boolean existsReview(Long reviewId);

    PageResponse<List<HomeRes.Review>> recommendReview(Long userId, Integer page, Integer size);

    void readReview(Long reviewId, Long userId);

    void reportReview(Long userId, ReviewReq.ReportReview reportReview);

    void deleteReview(Long reviewId, Long userId);

    HomeRes.ReviewInfo getRecommendedReview(Long userId, Long reviewId);
}
