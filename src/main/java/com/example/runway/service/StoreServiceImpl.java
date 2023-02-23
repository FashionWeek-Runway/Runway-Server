package com.example.runway.service;

import com.example.runway.convertor.ReviewConvertor;
import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.*;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final KeepRepository keepRepository;
    private final StoreImgRepository storeImgRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final AwsS3Service awsS3Service;
    private final OwnerFeedRepository ownerFeedRepository;
    private final KeepOwnerFeedRepository keepOwnerFeedRepository;
    private final UserCategoryRepository userCategoryRepository;

    public StoreRes.HomeList getMainHome(Long userId) {

        return null;
    }

    public List<String> getCategoryList(Long userId){
        List<UserCategory> category=userCategoryRepository.findByUserIdAndStatus(userId,true);
        return category.stream().map(e-> e.getCategory().getCategory()).collect(Collectors.toList());
    }

    @Override
    public StoreRes.StoreInfo getStoreDetail(User user, Long storeId) {
        StoreRepository.StoreInfo storeResult= storeRepository.getStoreInfo(storeId);

        return StoreConvertor.getStoreDetail(storeResult,keepRepository.existsByUserIdAndStoreId(user.getId(),storeId),getStoreImgList(storeId));
    }

    @Override
    public boolean checkStore(Long storeId) {
        return storeRepository.existsByIdAndStatus(storeId,true);
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
    public PageResponse<List<StoreRes.StoreBlog>> getStoreBlog(Long storeId, Integer page, Integer size) {

        return null;
    }

    @Override
    public void postStoreReview(Long storeId, Long userId, MultipartFile multipartFile) throws IOException {
        String imgUrl = awsS3Service.upload(multipartFile,"review");

        StoreReview storeReview = ReviewConvertor.UploadImg(storeId,userId,imgUrl);

        storeReviewRepository.save(storeReview);
    }

    @Override
    public PageResponse<List<StoreRes.StoreBoardList>> getStoreBoard(Long userId, Long storeId, Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);
        List<StoreRes.StoreBoardList> storeBoard=new ArrayList<>();
        Page<OwnerFeedRepository.StoreBoardList> storeBoardResult= ownerFeedRepository.getStoreBoardList(storeId,pageReq);

        storeBoardResult.forEach(
            result->storeBoard.add(new StoreRes.StoreBoardList(
                    result.getImgUrl(),
                    result.getStoreId(),
                    result.getTitle(),
                    result.getDay()
            ))
        );
        return new PageResponse<>(storeBoardResult.isLast(),storeBoard);
    }

    @Override
    public StoreRes.StoreBoard getStoreBoardById(Long userId, Long boardId) {
        OwnerFeedRepository.StoreBoard result = ownerFeedRepository.getStoreBoard(userId,boardId);
        List<String> categoryList= new ArrayList<>();
        if(result.getImgUrl()!=null){
            categoryList= Stream.of(result.getImgUrl().split(",")).collect(Collectors.toList());
        }
        return StoreConvertor.StoreBoard(result,userId,categoryList);
    }

    @Override
    public boolean existsBookMark(Long id, Long storeId) {
        return keepRepository.existsByUserIdAndStoreId(id,storeId);
    }

    @Override
    public void unCheckBookMark(Long userId, Long storeId) {
        keepRepository.deleteByUserIdAndStoreId(userId,storeId);
    }

    @Override
    public void checkBookMark(Long userId, Long storeId) {
        Keep keep = StoreConvertor.CheckBookMark(userId,storeId);
        keepRepository.save(keep);
    }

    @Override
    public boolean existsBookMarkFeed(Long userId, Long feedId) {
        return keepOwnerFeedRepository.existsByUserIdAndFeedId(userId,feedId);
    }

    @Override
    public void unCheckBookMarkFeed(Long userId, Long feedId) {
        keepOwnerFeedRepository.deleteByUserIdAndFeedId(userId,feedId);
    }

    @Override
    public void checkBookMarkFeed(Long userId, Long feedId) {
        KeepOwnerFeed keepOwnerFeed = StoreConvertor.CheckBookMarkFeed(userId,feedId);
        keepOwnerFeedRepository.save(keepOwnerFeed);
    }


    @Override
    public StoreRes.ReviewInfo getStoreReviewByReviewId(Long reviewId) {
        StoreReviewRepository.GetStoreReview result=storeReviewRepository.getStoreReview(reviewId);
        return StoreConvertor.StoreReview(result);
    }


    private List<String> getStoreImgList(Long storeId) {
        List<StoreImg> storeImg=storeImgRepository.findByStoreIdOrderBySequenceAsc(storeId);


        return storeImg.stream().map(StoreImg::getStoreImg).collect(Collectors.toList());
    }


    private int calculateMatchingScore(List<String> userCategory,List<String> storeCategory){
        int matchingCount=0;
        for(String category : userCategory){
            if(storeCategory.contains(category)){
                matchingCount++;
            }
        }
        return matchingCount;
    }

    @Override
    public List<HomeRes.StoreInfo> recommendStore(Long userId) {
        List<String> categoryList= getCategoryList(userId);

        List<StoreRepository.RecommendStore> recommendStoreResult=storeRepository.recommendStore(userId,categoryList);
        List<HomeRes.StoreInfo> storeInfo=new ArrayList<>();

        recommendStoreResult.forEach(
                result->{
                    storeInfo.add(new HomeRes.StoreInfo(
                            result.getBookmark(),
                            result.getImgUrl(),
                            result.getStoreId(),
                            result.getStoreName(),
                            result.getRegionInfo(),
                            Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                            result.getBookmarkCnt()
                    ));
                }
        );

        return getRecommendedShowrooms(categoryList,storeInfo);
    }

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


}
