package com.example.runway.service.user;

import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;

import java.util.List;

public interface UserService {
    void postUserLocation(User user, UserReq.UserLocation userLocation);

    void postUserCategory(Long userId, HomeReq.PostUserCategory postUserCategory);

    PageResponse<List<UserRes.Review>> getMyReview(Long id, Integer page, Integer size);

    UserRes.UserInfo getMyInfo(Long userId);

    UserRes.PatchUserInfo getUserProfile(Long userId);

    PageResponse<List<UserRes.StoreInfo>> getMyBookMarkStore(Long userId, Integer page, Integer size);

    UserRes.ReviewInfo getMyReviewDetail(Long id, Long reviewId);

    PageResponse<List<UserRes.Review>> getMyBookMarkReview(Long userId, Integer page, Integer size);

    UserRes.ReviewInfo getMyBookMarkReviewDetail(Long id, Long reviewId);
}
