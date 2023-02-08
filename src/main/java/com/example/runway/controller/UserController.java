package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BaseException;
import com.example.runway.jwt.TokenProvider;
import com.example.runway.service.RedisService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.runway.constants.CommonResponseStatus.ForbiddenException;
import static com.example.runway.constants.CommonResponseStatus.INVALID_REFRESH_TOKEN;


@Api(tags = "02-사용자 👤")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final RedisService redisService;
    private final TokenProvider tokenProvider;


    @Operation(summary = "02-01 토큰 재발급 👤", description = "액세스 토큰 만료시 재발급 요청 하는 API")
    @ApiImplicitParam(name="X-REFRESH-TOKEN",value = "리프레쉬 토큰값",dataType = "String",paramType = "header")
    @ResponseBody
    @PostMapping("/refresh")
    public CommonResponse<UserRes.ReIssueToken> reIssueToken(@AuthenticationPrincipal User user){

        Long userId = user.getId();
        String redisRT= redisService.getValues(String.valueOf(userId));

        if(redisRT==null){
            throw new BaseException(INVALID_REFRESH_TOKEN);

        }
        if(!redisRT.equals(tokenProvider.getRefreshToken())){
            throw new BaseException(ForbiddenException);
        }

        UserRes.ReIssueToken tokenRes=new UserRes.ReIssueToken(tokenProvider.createRefreshToken(userId));

        return CommonResponse.onSuccess(tokenRes);

    }


    // 토큰이 유효하다는 가정 하
    // 만약 토큰이 만료되었다면 재발급 요청
    @ApiOperation(value = "02-02 로그아웃 👤", notes = "로그아웃 요청 API")
    @ResponseBody
    @GetMapping("/logout")
    public CommonResponse<String> logOut(@AuthenticationPrincipal User user){

        //탈취된 토큰인지 검증
        Long userId = user.getId();

        //헤더에서 토큰 가져오기
        String accessToken = tokenProvider.getJwt();
        //jwt 에서 로그아웃 처리 & 오류처리 &
        tokenProvider.logOut(userId,accessToken);
        //TODO : FCM 설정 시 메소드 주석 삭제
        //logInService.deleteFcmToken(userId);
        String result="로그아웃 성공";
        return CommonResponse.onSuccess(result);

    }
}
