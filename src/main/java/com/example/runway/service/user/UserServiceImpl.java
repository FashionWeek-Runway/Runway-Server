package com.example.runway.service.user;

import com.example.runway.constants.Constants;
import com.example.runway.convertor.UserConvertor;
import com.example.runway.domain.User;
import com.example.runway.domain.UserCategory;
import com.example.runway.domain.pk.UserCategoryPk;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final StoreRepository storeRepository;
    private final LoginService loginService;
    private final SocialRepository socialRepository;

    @Override
    public void postUserLocation(User user, UserReq.UserLocation userLocation) {
        user.updateUserLocation(userLocation);
        userRepository.save(user);
    }

    @Override
    public void postUserCategory(Long userId, HomeReq.PostUserCategory postUserCategory) {

        userCategoryRepository.deleteByIdUserId(userId);
        saveUserCategoryList(userId,postUserCategory.getCategoryList());

    }

    @Override
    public PageResponse<List<UserRes.Review>> getMyReview(Long userId, Integer page, Integer size) {

        List<UserRes.Review> review=new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReviewRepository.GetReviewInfo> reviewResult=storeReviewRepository.GetReviewInfo(userId,pageReq);

        reviewResult.forEach(
                result->review.add(
                        new UserRes.Review(
                                result.getReviewId(),
                                result.getImgUrl(),
                                result.getRegionInfo()
                        )
                )
        );

        return new PageResponse<>(reviewResult.isLast(),review);
    }

    @Override
    public UserRes.UserInfo getMyInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        return UserConvertor.UserInfo(user.get());
    }

    @Override
    public UserRes.PatchUserInfo getUserProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return UserConvertor.UserProfile(user.get());
    }

    @Override
    public PageResponse<List<UserRes.StoreInfo>> getMyBookMarkStore(Long userId, Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreRepository.StoreInfoList> storeResult=storeRepository.getMyBookMarkStore(userId,pageReq);
        List<UserRes.StoreInfo> storeInfo=new ArrayList<>();

        storeResult.forEach(
                result->storeInfo.add(
                        new UserRes.StoreInfo(
                                result.getStoreId(),
                                result.getStoreImg(),
                                Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                                result.getStoreName()
                                )
                )
        );

        return new PageResponse<>(storeResult.isLast(),storeInfo);
    }

    @Override
    public UserRes.ReviewInfo getMyReviewDetail(Long userId, Long reviewId) {
        StoreReviewRepository.GetStoreReview result=storeReviewRepository.getMyReview(reviewId);

        Long prevReviewId = getPrevReviewId(userId, result.getCreatedAt(), result.getReviewId());
        Long nextReviewId = getNextReviewId(userId, result.getCreatedAt(), result.getReviewId());
        return UserConvertor.MyReviewDetail(result,new UserRes.ReviewInquiry(prevReviewId,nextReviewId),userId);
    }

    @Override
    public PageResponse<List<UserRes.Review>> getMyBookMarkReview(Long userId, Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReviewRepository.GetReviewInfo> reviewResult=storeReviewRepository.getMyBookMarkReview(userId,pageReq);
        List<UserRes.Review> review=new ArrayList<>();


        reviewResult.forEach(
                result->review.add(
                        new UserRes.Review(
                                result.getReviewId(),
                                result.getImgUrl(),
                                result.getRegionInfo()
                        )
                )
        );

        return new PageResponse<>(reviewResult.isLast(),review);



    }

    @Override
    public UserRes.ReviewInfo getMyBookMarkReviewDetail(Long userId, Long reviewId) {

        StoreReviewRepository.GetStoreReview result=storeReviewRepository.getMyBookmarkReview(reviewId,userId);

        Long prevReviewId = getBookMarkPrevReviewId(userId, result.getCreatedAt(), result.getReviewId());
        Long nextReviewId = getBookMarkNextReviewId(userId, result.getCreatedAt(), result.getReviewId());

        return UserConvertor.MyReviewDetail(result,new UserRes.ReviewInquiry(prevReviewId,nextReviewId),userId);
    }

    private Long getBookMarkNextReviewId(Long userId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findNextBookMarkReviewId(createdAt,userId, reviewId);
        Long nextId = null;
        if (result != null) {
            nextId = result.getId();
        }
        return nextId;
    }

    private Long getBookMarkPrevReviewId(Long userId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findPrevBookMarkReviewId(createdAt, userId, reviewId);
        Long prevId = null;
        if (result != null) {
            prevId = result.getId();
        }
        return prevId;
    }

    private Long getNextReviewId(Long userId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findNextMyReviewId(createdAt,userId, reviewId);
        Long nextId = null;
        if (result != null) {
            nextId = result.getId();
        }
        return nextId;
    }

    private Long getPrevReviewId(Long userId, LocalDateTime createdAt, Long reviewId) {
        StoreReviewRepository.GetReviewId result = storeReviewRepository.findPrevMuReviewId(createdAt, userId, reviewId);
        Long prevId = null;
        if (result != null) {
            prevId = result.getId();
        }
        return prevId;
    }

    public void saveUserCategoryList(Long userId,List<Long> userCategoryList) {

        List<UserCategory> userCategoryArrayList = new ArrayList<>();
        for (Long categoryId : userCategoryList) {
            UserCategory userCategory = UserCategory.builder().id(new UserCategoryPk(userId,categoryId)).build();
            userCategoryArrayList.add(userCategory);
        }



        userCategoryRepository.saveAll(userCategoryArrayList);
    }


    @Override
    public UserRes.SettingInfo getUserInfo(User user) {
        boolean social = user.getSocial() != null;

        boolean kakao=checkKakaoSync(user.getId());
        boolean apple=checkAppleSync(user.getId());

        return UserConvertor.SettingInfo(kakao,apple,user.getUsername(),social);
    }

    public boolean checkAppleSync(Long userId) {
        return socialRepository.existsByUserIdAndType(userId, Constants.apple);
    }

    public boolean checkKakaoSync(Long userId) {
        return socialRepository.existsByUserIdAndType(userId, Constants.kakao);
    }

}
