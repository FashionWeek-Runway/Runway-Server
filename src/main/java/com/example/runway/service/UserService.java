package com.example.runway.service;

import com.example.runway.exception.BaseException;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;

public interface UserService {
    UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo) throws BaseException;

    UserRes.Token signUp(UserReq.SignupUser signupUser) throws BaseException;

    boolean checkUserId(String username);

    boolean validationPassword(String password);

    boolean checkNickName(String nickname);

    boolean validationPhoneNumber(String phone);

    void updateFcmToken(Long userId, String token);
}
