package com.example.runway.service.user;

import com.example.runway.domain.User;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.ForbiddenException;

import java.io.IOException;

public interface AuthService {
    String getKakaoAccessToken(String code);

    UserRes.Token logInKakaoUser(UserReq.SocialLogin SocialLogin) throws ForbiddenException;

    UserRes.AppleLogin appleLogin(UserReq.SocialLogin SocialLogin);

    void syncKakaoUser(Long id, String accessToken);

    void syncAppleUser(Long id, String accessToken);

    void unSyncSocial(Long id, String social);

    void revoke(String code) throws IOException;
}
