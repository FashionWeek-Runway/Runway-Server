package com.example.runway.service.user;

import com.example.runway.domain.User;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;

import java.util.List;

public interface UserService {
    void postUserLocation(User user, UserReq.UserLocation userLocation);

    void postUserCategory(Long userId, HomeReq.PostUserCategory postUserCategory);

    List<UserRes.Review> getMyReview(Long id);
}