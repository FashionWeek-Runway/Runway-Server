package com.example.runway.service.store;

import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {

    void postStoreReview(Long storeId, Long userId, MultipartFile multipartFile) throws IOException;

    PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page, int size);

    StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId);

    boolean existsReview(Long reviewId);

    PageResponse<List<HomeRes.ReviewList>> recommendReview(Long userId, Integer page, Integer size);

    void readReview(Long reviewId, Long userId);
}
