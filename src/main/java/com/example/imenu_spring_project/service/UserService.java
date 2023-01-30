package com.example.imenu_spring_project.service;

import com.example.imenu_spring_project.common.CommonException;
import com.example.imenu_spring_project.dto.UserReq;
import com.example.imenu_spring_project.dto.UserRes;

public interface UserService {
    UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo) throws CommonException;

    UserRes.Token signUp(UserReq.SignupUser signupUser) throws CommonException;

    boolean checkUserId(String username);

    boolean validationPassword(String password);

    boolean checkNickName(String nickname);

    boolean validationPhoneNumber(String phone);

    void updateFcmToken(Long userId, String token);
}
