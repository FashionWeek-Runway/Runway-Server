package com.example.runway.service.store;

import com.example.runway.convertor.ReviewConvertor;
import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.ReviewRead;
import com.example.runway.domain.StoreReview;
import com.example.runway.domain.pk.ReviewReadPk;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.ReviewReadRepository;
import com.example.runway.repository.StoreReviewRepository;
import com.example.runway.service.util.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final AwsS3Service awsS3Service;
    private final StoreReviewRepository storeReviewRepository;
    private final StoreService storeService;
    private final ReviewReadRepository reviewReadRepository;

    @Override
    public void postStoreReview(Long storeId, Long userId, MultipartFile multipartFile) throws IOException {
        String imgUrl = awsS3Service.upload(multipartFile, "review");

        StoreReview storeReview = ReviewConvertor.UploadImg(storeId, userId, imgUrl);

        storeReviewRepository.save(storeReview);
    }

    @Override
    public PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page, int size) {

        List<StoreRes.StoreReview> storeReviewList = new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReview> storeReview = storeReviewRepository.findByStoreIdAndStatusOrderByCreatedAtDescIdAsc(storeId, true, pageReq);

        for (StoreReview review : storeReview) {
            StoreRes.StoreReview storeReviewDto = StoreConvertor.StoreReviewBuilder(review);
            storeReviewList.add(storeReviewDto);
        }

        return new PageResponse<>(storeReview.isLast(), storeReviewList);
    }

    @Override
    public StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId) {
        StoreReviewRepository.GetStoreReview result = storeReviewRepository.getStoreReview(reviewId);
        Long prevReviewId = getPrevReviewId(result.getStoreId(), result.getCreatedAt(), result.getReviewId());
        Long nextReviewId = getNextReviewId(result.getStoreId(), result.getCreatedAt(), result.getReviewId());
        System.out.println(result.getCreatedAt());
        System.out.println(result.getReviewId());
        System.out.println("prevReviewID:" + prevReviewId);
        System.out.println("nextReviewId:" + nextReviewId);

        return StoreConvertor.StoreReview(result, prevReviewId, nextReviewId);
    }

    private Long getNextReviewId(Long storeId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findNextReviewId(createdAt, storeId, reviewId);
        Long nextId = null;
        if (result != null) {
            nextId = result.getId();
        }
        return nextId;
    }

    private Long getPrevReviewId(Long storeId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findPrevReviewId(createdAt, storeId, reviewId);
        Long prevId = null;
        if (result != null) {
            prevId = result.getId();
        }
        return prevId;
    }

    @Override
    public boolean existsReview(Long reviewId) {
        return storeReviewRepository.existsByIdAndStatus(reviewId, true);
    }

    @Override
    public PageResponse<List<HomeRes.Review>> recommendReview(Long userId, Integer page, Integer size) {
        List<String> categoryList = storeService.getCategoryList(userId);


        List<HomeRes.Review> review = new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReviewRepository.GetReview> reviewResult=storeReviewRepository.RecommendReview(userId,categoryList,pageReq);

        reviewResult.forEach(
                result-> review.add(
                        new HomeRes.Review(
                                result.getReviewId(),
                                result.getImgUrl(),
                                result.getRegionInfo(),
                                result.getIsRead()
                        )
                )
        );

        return new PageResponse<>(reviewResult.isLast(),review);
    }

    @Override
    public void readReview(Long reviewId, Long userId) {
        ReviewReadPk reviewReadPk = ReviewReadPk.builder().reviewId(reviewId).userId(userId).build();

        if (!reviewReadRepository.existsByIdReviewIdAndIdUserId(reviewId, userId)) {
            ReviewRead reviewRead = ReviewRead.builder().id(reviewReadPk).build();
            reviewReadRepository.save(reviewRead);
        }
    }


}
