package com.example.runway.service.store;

import com.example.runway.convertor.ReviewConvertor;
import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.ReviewRead;
import com.example.runway.domain.ReviewReport;
import com.example.runway.domain.StoreReview;
import com.example.runway.domain.pk.ReviewReadPk;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.ReviewReq;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.NotFoundException;
import com.example.runway.repository.ReviewReadRepository;
import com.example.runway.repository.ReviewReportRepository;
import com.example.runway.repository.StoreReviewRepository;
import com.example.runway.service.user.UserService;
import com.example.runway.service.util.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_REVIEW;
import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_REVIEW_DELETE;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final AwsS3Service awsS3Service;
    private final StoreReviewRepository storeReviewRepository;
    private final UserService userService;
    private final ReviewReadRepository reviewReadRepository;
    private final ReviewReportRepository reviewReportRepository;

    @Override
    public void postStoreReview(Long storeId, Long userId, byte[] bytes) throws IOException {
        String imgUrl = awsS3Service.uploadByteCode(bytes, "review");

        StoreReview storeReview = ReviewConvertor.UploadImg(storeId, userId, imgUrl);

        storeReviewRepository.save(storeReview);
    }


    @Override
    public void postStoreReviewImg(Long storeId, Long userId, MultipartFile multipartFile) throws IOException {
        String imgUrl = awsS3Service.upload(multipartFile,"review");

        StoreReview storeReview = ReviewConvertor.UploadImg(storeId, userId, imgUrl);

        storeReviewRepository.save(storeReview);
    }

    @Override
    public PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page, int size) {

        List<StoreRes.StoreReview> storeReviewList = new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReview> storeReview = storeReviewRepository.findByStoreIdAndDeletedOrderByCreatedAtDescIdAsc(storeId, true, pageReq);

        for (StoreReview review : storeReview) {
            StoreRes.StoreReview storeReviewDto = StoreConvertor.StoreReviewBuilder(review);
            storeReviewList.add(storeReviewDto);
        }

        return new PageResponse<>(storeReview.isLast(), storeReviewList);
    }

    @Override
    public StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId, Long userId) {
        StoreReviewRepository.GetStoreReview result = storeReviewRepository.getStoreReview(reviewId,userId);
        StoreRes.ReviewResult prevReviewId = getPrevReviewId(result.getStoreId(), result.getCreatedAt(), result.getReviewId());
        StoreRes.ReviewResult nextReviewId = getNextReviewId(result.getStoreId(), result.getCreatedAt(), result.getReviewId());

        return StoreConvertor.StoreReview(result, prevReviewId, nextReviewId,userId);
    }

    private StoreRes.ReviewResult getNextReviewId(Long storeId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findNextReviewId(createdAt, storeId, reviewId);

        Long nextId = null;
        String nextImgUrl = null;
        if (result != null) {
            nextId = result.getId();
            nextImgUrl = result.getImgUrl();
        }
        return new StoreRes.ReviewResult(nextId,nextImgUrl);
    }

    private StoreRes.ReviewResult getPrevReviewId(Long storeId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findPrevReviewId(createdAt, storeId, reviewId);
        Long prevId = null;
        String prevReviewImg = null;
        if (result != null) {
            prevId = result.getId();
            prevReviewImg = result.getImgUrl();
        }
        return new StoreRes.ReviewResult(prevId, prevReviewImg);
    }

    @Override
    public boolean existsReview(Long reviewId) {
        return storeReviewRepository.existsByIdAndDeleted(reviewId, true);
    }

    @Override
    public PageResponse<List<HomeRes.Review>> recommendReview(Long userId, Integer page, Integer size) {
        List<String> categoryList = userService.getCategoryList(userId);


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
        ReviewRead reviewRead = ReviewRead.builder().id(reviewReadPk).build();
        reviewReadRepository.save(reviewRead);
    }

    @Override
    public void reportReview(Long userId, ReviewReq.ReportReview reportReview) {
        ReviewReport reviewReport = ReviewConvertor.ReportReview(userId,reportReview);
        reviewReportRepository.save(reviewReport);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        StoreReview storeReview = (StoreReview) storeReviewRepository.findByIdAndStatus(reviewId,true).orElseThrow(()-> new NotFoundException(NOT_EXIST_REVIEW));;

        if(!storeReview.getUserId().equals(userId)) throw new BadRequestException(NOT_EXIST_REVIEW_DELETE);

        storeReview.delete(false);

        storeReviewRepository.save(storeReview);

    }

    @Override
    public HomeRes.ReviewInfo getRecommendedReview(Long userId, Long reviewId) {
        List<String> categoryList = userService.getCategoryList(userId);

        StoreReviewRepository.GetStoreReview result = storeReviewRepository.getStoreReviewRecommend(reviewId,userId,categoryList);



        StoreRes.ReviewResult prevReviewId = getPrevRecommendId(result.getCategoryScore(), result.getCreatedAt(), result.getReviewId(),categoryList);
        StoreRes.ReviewResult nextReviewId = getNextRecommendId(result.getCategoryScore(), result.getCreatedAt(), result.getReviewId(),categoryList);

        return StoreConvertor.StoreReviewRecommend(result, prevReviewId, nextReviewId,userId);
    }


    private StoreRes.ReviewResult getNextRecommendId(int categoryScore, LocalDateTime createdAt, Long reviewId, List<String> categoryList) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findNextRecommendReviewId(createdAt, categoryScore, reviewId,categoryList);

        Long nextId = null;
        String imgUrl = null;
        if (result != null) {
            nextId = result.getId();
            imgUrl = result.getImgUrl();

        }
        return new StoreRes.ReviewResult(nextId,imgUrl);
    }

    private StoreRes.ReviewResult getPrevRecommendId(int categoryScore, LocalDateTime createdAt, Long reviewId, List<String> categoryList) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findPrevRecommendReviewId(createdAt, categoryScore, reviewId,categoryList);

        Long prevId = null;
        String imgUrl = null;
        if (result != null) {
            prevId = result.getId();
            imgUrl = result.getImgUrl();
        }

        return new StoreRes.ReviewResult(prevId,imgUrl);
    }


}
