package com.example.runway.service;

import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;
import com.example.runway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void postUserLocation(User user, UserReq.UserLocation userLocation) {
        user.updateUserLocation(userLocation);
        userRepository.save(user);
    }
}
