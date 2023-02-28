package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.jwt.TokenProvider;
import com.example.runway.service.util.RedisService;
import com.example.runway.service.user.UserService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.INVALID_REFRESH_TOKEN;


@Api(tags = "02-사용자 👤")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final UserService userService;


    @Operation(summary = "02-01 토큰 재발급 👤", description = "액세스 토큰 만료시 재발급 요청 하는 API")
    @ApiImplicitParam(name="X-REFRESH-TOKEN",value = "리프레쉬 토큰값",dataType = "String",paramType = "header")
    @ResponseBody
    @PostMapping("/refresh")
    public CommonResponse<UserRes.ReIssueToken> reIssueToken(){
        log.info("reIssue-token");
        log.info("api = reIssue-token 02-01");
        String refreshToken = tokenProvider.getRefreshToken();

        Long userId=tokenProvider.getUserIdByRefreshToken(refreshToken);

        String redisRT= redisService.getValues(String.valueOf(userId));

        if(redisRT==null){
            throw new BadRequestException(INVALID_REFRESH_TOKEN);

        }


        UserRes.ReIssueToken tokenRes=new UserRes.ReIssueToken(tokenProvider.createToken(userId));

        return CommonResponse.onSuccess(tokenRes);

    }


    // 토큰이 유효하다는 가정 하
    // 만약 토큰이 만료되었다면 재발급 요청
    @ApiOperation(value = "02-02 로그아웃 👤", notes = "로그아웃 요청 API")
    @ResponseBody
    @GetMapping("/logout")
    public CommonResponse<String> logOut(@AuthenticationPrincipal User user){

        log.info("logout");
        log.info("api = logout 02-02");
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

    /*
    // 토큰이 유효하다는 가정 하
    // 만약 토큰이 만료되었다면 재발급 요청
    @ApiOperation(value = "02-03 유저 위치 저장 👤", notes = "위치 저장 API")
    @ResponseBody
    @PostMapping("/location")
    public CommonResponse<String> postUserLocation(@AuthenticationPrincipal User user, @RequestBody UserReq.UserLocation userLocation){

        log.info("post-location");
        log.info("api = post-location 02-03");
        userService.postUserLocation(user,userLocation);
        return CommonResponse.onSuccess("위치 정보 저장 성공");

    }
     */

    @ApiOperation(value = "02-03 마이페이지 조회(사장님 여부까지 포함) 👤 FRAME MY")
    @GetMapping("/")
    public CommonResponse<UserRes.UserInfo> getMyInfo(@AuthenticationPrincipal User user){
        log.info("get-my-info");
        log.info("api = get-my-info 02-03");

        Long userId = user.getId();
        UserRes.UserInfo userInfo = userService.getMyInfo(userId);
        return CommonResponse.onSuccess(userInfo);
    }


    @ApiOperation(value = "02-04 프로필 편집을 위한 기존 데이터 GET 👤 FRAME MY")
    @GetMapping("/profile")
    public CommonResponse<UserRes.PatchUserInfo> getUserProfile(@AuthenticationPrincipal User user){
        log.info("get-profile-info");
        log.info("api = get-profile-info 02-04");

        Long userId = user.getId();
        UserRes.PatchUserInfo userInfo = userService.getUserProfile(userId);
        return CommonResponse.onSuccess(userInfo);
    }

    /*
    @ApiOperation(value = "02-05 프로필 편집  👤 FRAME MY")
    @PatchMapping("/profile")
    public CommonResponse<String> modifyUserProfile(@AuthenticationPrincipal User user){
        return CommonResponse.onSuccess("변경 성공");
    }
     */

    @ApiOperation(value = "02-06 내가 작성한 리뷰 보기 👤 FRAME MY")
    @GetMapping("/review")
    public CommonResponse<PageResponse<List<UserRes.Review>>> getMyReview(@AuthenticationPrincipal User user,
                                                            @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-review");
        log.info("api = get-my-review 02-06");
        PageResponse<List<UserRes.Review>> review=userService.getMyReview(user.getId(),page,size);
        return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "02-07 내가 작성한 리뷰 상세 조회 👤 FRAME MY_REVIEW")
    @GetMapping("/review/detail/{reviewId}")
    public CommonResponse<UserRes.ReviewInfo> getMyReviewDetail(@AuthenticationPrincipal User user, @Parameter(description = "review 리뷰 Id값") @PathVariable Long reviewId){
        log.info("get-review-detail");
        log.info("api = get-my-review-detail 02-07");
        UserRes.ReviewInfo review=userService.getMyReviewDetail(user.getId(),reviewId);
        return CommonResponse.onSuccess(review);
    }


    @ApiOperation(value = "02-08 내가 북마크한 쇼룸 리스트 보기 👤 FRAME MY")
    @GetMapping("/store")
    public CommonResponse<PageResponse<List<UserRes.StoreInfo>>> getMyBookMarkStore(@AuthenticationPrincipal User user,
                                                                                    @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                    @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-bookmark-store");
        log.info("api = get-my-bookmark-store 02-08");
        Long userId=user.getId();
        PageResponse<List<UserRes.StoreInfo>> storeInfo=userService.getMyBookMarkStore(userId,page,size);

        return CommonResponse.onSuccess(storeInfo);
    }


    @ApiOperation(value = "02-09 내가 북마크한 리뷰 리스트 보기 👤 FRAME MY")
    @GetMapping("/bookmark/review")
    public CommonResponse<PageResponse<List<UserRes.Review>>> getMyBookMarkReview(@AuthenticationPrincipal User user,
                                                                                    @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                    @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-bookmark-store");
        log.info("api = get-my-bookmark-store 02-08");
        Long userId=user.getId();
        PageResponse<List<UserRes.Review>> review=userService.getMyBookMarkReview(userId,page,size);

        return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "02-10 내가 북마크한 리뷰 상세 조회 👤 FRAME MY_REVIEW")
    @GetMapping("/bookmark/review/detail/{reviewId}")
    public CommonResponse<UserRes.ReviewInfo> getMyBookMarkReviewDetail(@AuthenticationPrincipal User user, @Parameter(description = "review 리뷰 Id값") @PathVariable Long reviewId){
        log.info("get-review-detail");
        log.info("api = get-my-review-detail 02-07");
        UserRes.ReviewInfo review=userService.getMyBookMarkReviewDetail(user.getId(),reviewId);
        return CommonResponse.onSuccess(review);
    }

    //@ApiOperation(value = "02-07 내가 북마크한 사장님 소식 리스트 보기 👤 FRAME MY")


    //@ApiOperation(value = "02-08 사장님인 경우 내가 쓴 글 조회 👤 FRAME MY")


    @ApiOperation(value = "02-11 개인정보 관리 조회 👤 FRAME SETTINGS 01")
    @GetMapping("/info")
    public CommonResponse<UserRes.SettingInfo> getUserInfo(@AuthenticationPrincipal User user){
        UserRes.SettingInfo settingInfo =  userService.getUserInfo(user);
        return CommonResponse.onSuccess(settingInfo);
    }



}
