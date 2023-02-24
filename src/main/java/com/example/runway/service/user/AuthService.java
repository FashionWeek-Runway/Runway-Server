package com.example.runway.service.user;

import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.ForbiddenException;

public interface AuthService {
    String getKakaoAccessToken(String code);

    UserRes.Token logInKakaoUser(UserReq.SocialLogin SocialLogin) throws ForbiddenException;

    UserRes.AppleLogin appleLogin(UserReq.SocialLogin SocialLogin);

}
