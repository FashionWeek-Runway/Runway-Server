package com.example.runway.service.store;

import com.example.runway.convertor.ReviewConvertor;
import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.Store;
import com.example.runway.domain.StoreReview;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.StoreReviewRepository;
import com.example.runway.service.util.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final AwsS3Service awsS3Service;
    private final StoreReviewRepository storeReviewRepository;
    private final StoreService storeService;
    @PersistenceContext
    private EntityManager entityManager;

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

        Page<StoreReview> storeReview = storeReviewRepository.findByStoreIdAndStatusOrderByCreatedAtDescIdAsc(storeId, true, pageReq);

        for (StoreReview review : storeReview) {
            StoreRes.StoreReview storeReviewDto = StoreConvertor.StoreReviewBuilder(review);
            storeReviewList.add(storeReviewDto);
        }

        return new PageResponse<>(storeReview.isLast(),storeReviewList);
    }

    @Override
    public StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId) {
        entityManager.clear();
        StoreReviewRepository.GetStoreReview result=storeReviewRepository.getStoreReview(reviewId);
        Long prevReviewId=getPrevReviewId(result.getStoreId(), result.getCreatedAt(),result.getReviewId());
        Long nextReviewId=getNextReviewId(result.getStoreId(), result.getCreatedAt(),result.getReviewId());
        System.out.println(result.getCreatedAt());
        System.out.println(result.getReviewId());
        System.out.println("prevReviewID:"+prevReviewId);
        System.out.println("nextReviewId:"+nextReviewId);

        return StoreConvertor.StoreReview(result,prevReviewId,nextReviewId);
    }

    private Long getNextReviewId(Long storeId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result=storeReviewRepository.findNextReviewId(createdAt,storeId,reviewId);
        Long nextId = null;
        if(result!=null)
        {
            nextId=result.getId();
        }
        return nextId;
    }

    private Long getPrevReviewId(Long storeId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result=storeReviewRepository.findPrevReviewId(createdAt, storeId, reviewId);
        Long prevId = null;
        if(result!=null)
        {
            prevId=result.getId();
        }
        return prevId;
    }

    @Override
    public boolean existsReview(Long reviewId) {
        return storeReviewRepository.existsByIdAndStatus(reviewId,true);
    }

    @Override
    public PageResponse<List<HomeRes.ReviewList>> recommendReview(Long userId, Integer page, Integer size) {
        List<String> categoryList= storeService.getCategoryList(userId);

        StoreReviewRepository.GetCountAllReview result=storeReviewRepository.CountReview(categoryList);

        int maxSize=result.getSize();

        List<HomeRes.ReviewList> unReadReviews=new ArrayList<>();

        return null;
    }


    /*
    public List<HomeRes.StoreInfo> getRecommendedShowrooms(List<String> userCategory, List<HomeRes.StoreInfo> storeInfo) {
        return sortShowroomsByMatchingScore(userCategory, storeInfo);
    }

    public List<HomeRes.StoreInfo> sortShowroomsByMatchingScore(List<String> userCategory, List<HomeRes.StoreInfo> storeInfo) {

        storeInfo.sort((a, b) -> {
            int scoreCompare = calculateMatchingScore(userCategory, b.getCategoryList()) - calculateMatchingScore(userCategory, a.getCategoryList());

            if (scoreCompare != 0) {
                return scoreCompare; // 매칭 점수가 다를 경우 매칭 점수가 높은 쇼룸이 우선순위가 높도록 정렬
            } else {
                return b.getBookmarkCnt() - a.getBookmarkCnt(); // 매칭 점수가 같을 경우 북마크 갯수가 많은 쇼룸이 우선순위가 높도록 정렬
            }
        });
        return storeInfo;
    }
     private int calculateMatchingScore(List<String> userCategory,List<String> storeCategory){
        int matchingCount=0;
        for(String category : userCategory){
            if(storeCategory.contains(category)){
                matchingCount++;
            }
        }
        return matchingCount;

     */
}
