package com.example.runway.service.store;

import com.example.runway.convertor.ReviewConvertor;
import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.StoreReview;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.StoreReviewRepository;
import com.example.runway.service.util.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final AwsS3Service awsS3Service;
    private final StoreReviewRepository storeReviewRepository;
    @Override
    public void postStoreReview(Long storeId, Long userId, MultipartFile multipartFile) throws IOException {
        String imgUrl = awsS3Service.upload(multipartFile,"review");

        StoreReview storeReview = ReviewConvertor.UploadImg(storeId,userId,imgUrl);

        storeReviewRepository.save(storeReview);
    }

    @Override
    public PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page, int size) {

        List<StoreRes.StoreReview> storeReviewList=new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReview> storeReview = storeReviewRepository.findByStoreIdAndStatusOrderByCreatedAtDesc(storeId, true, pageReq);

        for (StoreReview review : storeReview) {
            StoreRes.StoreReview storeReviewDto = StoreConvertor.StoreReviewBuilder(review);
            storeReviewList.add(storeReviewDto);
        }

        return new PageResponse<>(storeReview.isLast(),storeReviewList);
    }

    @Override
    public StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId) {
        StoreReviewRepository.GetStoreReview result=storeReviewRepository.getStoreReview(reviewId);
        return StoreConvertor.StoreReview(result);
    }

    @Override
    public boolean existsReview(Long reviewId) {
        return storeReviewRepository.existsByIdAndStatus(reviewId,true);
    }
}
