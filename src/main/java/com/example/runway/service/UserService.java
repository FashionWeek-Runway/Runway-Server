package com.example.runway.service;

import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;

public interface UserService {
    void postUserLocation(User user, UserReq.UserLocation userLocation);
}
