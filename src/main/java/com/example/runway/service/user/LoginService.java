package com.example.runway.service.user;

import com.example.runway.exception.BaseException;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LoginService {
    UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo) throws BaseException;

    UserRes.SignUp signUp(MultipartFile multipartFile,UserReq.SignupUser signupUser) throws BaseException, IOException;

    boolean checkuserId(String username);

    boolean validationPassword(String password);

    boolean checkNickName(String nickname);

    boolean validationPhoneNumber(String phone);

    void updateFcmToken(Long userId, String token);

    void modifyPassword(UserReq.PostPassword postPassword);

    UserRes.GenerateToken createToken(Long userId);


    void countUserPhone(String phone);

    UserRes.SignUp signUpSocial(UserReq.SocialSignUp socialSignUp) throws IOException;

    List<String> getCategoryNameList(List<Long> categoryList);
}
