package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.constants.Constants;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.jwt.TokenProvider;
import com.example.runway.service.user.AuthService;
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
import java.io.IOException;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.*;


@Api(tags = "02-μ¬μ©μ π€")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AuthService authService;


    @Operation(summary = "02-01 ν ν° μ¬λ°κΈ π€", description = "μ‘μΈμ€ ν ν° λ§λ£μ μ¬λ°κΈ μμ²­ νλ API")
    @ApiImplicitParam(name="X-REFRESH-TOKEN",value = "λ¦¬νλ μ¬ ν ν°κ°",dataType = "String",paramType = "header")
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


    // ν ν°μ΄ μ ν¨νλ€λ κ°μ  ν
    // λ§μ½ ν ν°μ΄ λ§λ£λμλ€λ©΄ μ¬λ°κΈ μμ²­
    @ApiOperation(value = "02-02 λ‘κ·Έμμ π€", notes = "λ‘κ·Έμμ μμ²­ API")
    @ResponseBody
    @GetMapping("/logout")
    public CommonResponse<String> logOut(@AuthenticationPrincipal User user){

        log.info("logout");
        log.info("api = logout 02-02");
        //νμ·¨λ ν ν°μΈμ§ κ²μ¦
        Long userId = user.getId();

        //ν€λμμ ν ν° κ°μ Έμ€κΈ°
        String accessToken = tokenProvider.getJwt();
        //jwt μμ λ‘κ·Έμμ μ²λ¦¬ & μ€λ₯μ²λ¦¬ &
        tokenProvider.logOut(userId,accessToken);
        //TODO : FCM μ€μ  μ λ©μλ μ£Όμ μ­μ 
        //logInService.deleteFcmToken(userId);
        String result="λ‘κ·Έμμ μ±κ³΅";
        return CommonResponse.onSuccess(result);
    }

    /*
    // ν ν°μ΄ μ ν¨νλ€λ κ°μ  ν
    // λ§μ½ ν ν°μ΄ λ§λ£λμλ€λ©΄ μ¬λ°κΈ μμ²­
    @ApiOperation(value = "02-03 μ μ  μμΉ μ μ₯ π€", notes = "μμΉ μ μ₯ API")
    @ResponseBody
    @PostMapping("/location")
    public CommonResponse<String> postUserLocation(@AuthenticationPrincipal User user, @RequestBody UserReq.UserLocation userLocation){

        log.info("post-location");
        log.info("api = post-location 02-03");
        userService.postUserLocation(user,userLocation);
        return CommonResponse.onSuccess("μμΉ μ λ³΄ μ μ₯ μ±κ³΅");

    }
     */

    @ApiOperation(value = "02-03 λ§μ΄νμ΄μ§ μ‘°ν(μ¬μ₯λ μ¬λΆκΉμ§ ν¬ν¨) π€ FRAME MY",notes = "λ§μ΄νμ΄μ§ μ‘°ν")
    @GetMapping("/")
    public CommonResponse<UserRes.UserInfo> getMyInfo(@AuthenticationPrincipal User user){
        log.info("get-my-info");
        log.info("api = get-my-info 02-03");

        Long userId = user.getId();
        UserRes.UserInfo userInfo = userService.getMyInfo(userId);
        return CommonResponse.onSuccess(userInfo);
    }


    @ApiOperation(value = "02-04 νλ‘ν νΈμ§μ μν κΈ°μ‘΄ λ°μ΄ν° GET π€ FRAME MY",notes = "νλ‘ν νΈμ§ λ°μ΄ν° μ‘°ν")
    @GetMapping("/profile")
    public CommonResponse<UserRes.PatchUserInfo> getUserProfile(@AuthenticationPrincipal User user){
        log.info("get-profile-info");
        log.info("api = get-profile-info 02-04");

        Long userId = user.getId();
        UserRes.PatchUserInfo userInfo = userService.getUserProfile(userId);
        return CommonResponse.onSuccess(userInfo);
    }



    @ApiOperation(value = "02-05 νλ‘ν νΈμ§  π€ FRAME MY",notes = "μ΄λ―Έμ§ νμΌ λ³κ²½ν  κ²½μ° multipart μ λ£μ΄μ£Όμκ³ , μ΄λ―Έμ§ λ³κ²½ μν  μ multipart null κ°μΌλ‘ λ³΄λ΄μ£ΌμΈμ μμ΄λλ κΈ°μ‘΄ μμ΄λκ°+λ³κ²½ν  μμ΄λκ° λμ€ νλ λ³΄λ΄μλ©΄ λ©λλ€")
    @PatchMapping("/profile")
    public CommonResponse<UserRes.ModifyUser> modifyUserProfile(@ModelAttribute UserReq.ModifyProfile modifyProfile,@AuthenticationPrincipal User user) throws IOException {
        UserRes.ModifyUser modifyUser=userService.modifyUserProfile(user,modifyProfile);
        return CommonResponse.onSuccess(modifyUser);
    }




    @ApiOperation(value = "02-06 λ΄κ° μμ±ν λ¦¬λ·° λ³΄κΈ° π€ FRAME MY",notes = "λ΄κ° μμ±ν λ¦¬λ·° λͺ¨μλ³΄κΈ°")
    @GetMapping("/review")
    public CommonResponse<PageResponse<List<UserRes.Review>>> getMyReview(@AuthenticationPrincipal User user,
                                                            @Parameter(description = "νμ΄μ§", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                            @Parameter(description = "νμ΄μ§ μ¬μ΄μ¦", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-review");
        log.info("api = get-my-review 02-06");
        PageResponse<List<UserRes.Review>> review=userService.getMyReview(user.getId(),page,size);
        return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "02-07 λ΄κ° μμ±ν λ¦¬λ·° μμΈ μ‘°ν π€ FRAME MY_REVIEW",notes = "λ΄κ° μμ±ν λ¦¬λ·° μμΈ μ‘°ν prev,next Id")
    @GetMapping("/review/detail/{reviewId}")
    public CommonResponse<UserRes.ReviewInfo> getMyReviewDetail(@AuthenticationPrincipal User user, @Parameter(description = "review λ¦¬λ·° Idκ°") @PathVariable Long reviewId){
        log.info("get-review-detail");
        log.info("api = get-my-review-detail 02-07");
        UserRes.ReviewInfo review=userService.getMyReviewDetail(user.getId(),reviewId);
        return CommonResponse.onSuccess(review);
    }


    @ApiOperation(value = "02-08 λ΄κ° λΆλ§ν¬ν μΌλ£Έ λ¦¬μ€νΈ λ³΄κΈ° π€ FRAME MY",notes = "λ΄κ° λΆλ§ν¬ν μΌλ£Έ λͺ¨μλ³΄κΈ°")
    @GetMapping("/store")
    public CommonResponse<PageResponse<List<UserRes.StoreInfo>>> getMyBookMarkStore(@AuthenticationPrincipal User user,
                                                                                    @Parameter(description = "νμ΄μ§", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                    @Parameter(description = "νμ΄μ§ μ¬μ΄μ¦", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-bookmark-store");
        log.info("api = get-my-bookmark-store 02-08");
        Long userId=user.getId();
        PageResponse<List<UserRes.StoreInfo>> storeInfo=userService.getMyBookMarkStore(userId,page,size);

        return CommonResponse.onSuccess(storeInfo);
    }


    @ApiOperation(value = "02-09 λ΄κ° λΆλ§ν¬ν λ¦¬λ·° λ¦¬μ€νΈ λ³΄κΈ° π€ FRAME MY",notes = "λ΄κ° λΆλ§ν¬ν λ¦¬λ·° λͺ¨μλ³΄κΈ°")
    @GetMapping("/bookmark/review")
    public CommonResponse<PageResponse<List<UserRes.Review>>> getMyBookMarkReview(@AuthenticationPrincipal User user,
                                                                                    @Parameter(description = "νμ΄μ§", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                    @Parameter(description = "νμ΄μ§ μ¬μ΄μ¦", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-bookmark-store");
        log.info("api = get-my-bookmark-store 02-08");
        Long userId=user.getId();
        PageResponse<List<UserRes.Review>> review=userService.getMyBookMarkReview(userId,page,size);

        return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "02-10 λ΄κ° λΆλ§ν¬ν λ¦¬λ·° μμΈ μ‘°ν π€ FRAME MY_REVIEW",notes = "λΆλ§ν¬ν λ¦¬λ·° μμΈλ³΄κΈ° prev,next Id")
    @GetMapping("/bookmark/review/detail/{reviewId}")
    public CommonResponse<UserRes.ReviewInfo> getMyBookMarkReviewDetail(@AuthenticationPrincipal User user, @Parameter(description = "review λ¦¬λ·° Idκ°") @PathVariable Long reviewId){
        log.info("get-review-detail");
        log.info("api = get-my-review-detail 02-07");
        UserRes.ReviewInfo review=userService.getMyBookMarkReviewDetail(user.getId(),reviewId);
        return CommonResponse.onSuccess(review);
    }


    @ApiOperation(value = "02-11 κ°μΈμ λ³΄ κ΄λ¦¬ μ‘°ν π€ FRAME SETTING 02,03",
            notes = "social κ°μ΄ true = FRAME setting 03 , false = FRAME setting 02" +
                    " kakao,apple boolean κ°μΌλ‘ νλ©΄μ λ³΄μ¬μ£Όλ©΄ λ©λλ€!")
    @GetMapping("/info")
    public CommonResponse<UserRes.SettingInfo> getUserInfo(@AuthenticationPrincipal User user){
        UserRes.SettingInfo settingInfo =  userService.getUserInfo(user);
        return CommonResponse.onSuccess(settingInfo);
    }

    @ApiOperation(value = "02-12 κ°μΈμ λ³΄ μΉ΄μΉ΄μ€ μ°λ π€ FRAME SETTING 02",notes = "μΉ΄μΉ΄μ€ μ°λ")
    @PostMapping("/info/kakao")
    public CommonResponse<String> syncKakaoUser(@AuthenticationPrincipal User user, @RequestBody UserReq.SocialLogin socialLogin){
        authService.syncKakaoUser(user.getId(),socialLogin.getAccessToken());
        return CommonResponse.onSuccess("μ°λ μ±κ³΅");
   }

    @ApiOperation(value = "02-13 κ°μΈμ λ³΄ μ ν μ°λ π€ FRAME SETTING 02",notes = "μ ν μ°λ")
    @PostMapping("/info/apple")
    public CommonResponse<String> syncAppleUser(@AuthenticationPrincipal User user, @RequestBody UserReq.SocialLogin socialLogin){
        authService.syncAppleUser(user.getId(),socialLogin.getAccessToken());
        return CommonResponse.onSuccess("μ°λ μ±κ³΅");
    }


    @ApiOperation(value = "02-14 κ°μΈμ λ³΄ μΉ΄μΉ΄μ€ μ°λ ν΄μ§ π€ FRAME SETTING 02",notes = "μΉ΄μΉ΄μ€ μ°λ ν΄μ§")
    @DeleteMapping("/info/kakao")
    public CommonResponse<String> unSyncKakaoUser(@AuthenticationPrincipal User user){
        if(!userService.checkSocialUser(user.getId(),Constants.kakao))throw new BadRequestException(NOT_EXIST_SOCIAL);
        authService.unSyncSocial(user.getId(),Constants.kakao);
        return CommonResponse.onSuccess("μ°λ μ±κ³΅");
    }

    @ApiOperation(value = "02-15 κ°μΈμ λ³΄ μ ν μ°λ ν΄μ§ π€ FRAME SETTING 02",notes = "μ ν μ°λ ν΄μ§")
    @DeleteMapping("/info/apple")
    public CommonResponse<String> unSyncAppleUser(@AuthenticationPrincipal User user,@RequestBody UserReq.AppleCode appleCode) throws IOException {
        if(!userService.checkSocialUser(user.getId(),Constants.apple))throw new BadRequestException(NOT_EXIST_SOCIAL);
        authService.unSyncSocial(user.getId(), Constants.apple);
        authService.revoke(appleCode.getCode());
        return CommonResponse.onSuccess("μ°λ μ±κ³΅");
    }

    @ApiOperation(value = "02-16 κ°μΈμ λ³΄ λΉλ°λ²νΈλ³κ²½ π€ FRAME SETTING 02",notes = "μ ν μ°λ ν΄μ§")
    @PatchMapping("/password")
    public CommonResponse<String> modifyPassword(@AuthenticationPrincipal User user,@RequestBody UserReq.UserPassword userPassword){
        userService.modifyPassword(user,userPassword);
        return CommonResponse.onSuccess("λ³κ²½ μ±κ³΅");
    }

    @ApiOperation(value = "02-17 κ°μΈμ λ³΄ κΈ°μ‘΄ λΉλ°λ²νΈ νμΈ π€ FRAME SETTING 02",notes = "μ ν μ°λ ν΄μ§")
    @PostMapping("/password")
    public CommonResponse<String> checkPassword(@AuthenticationPrincipal User user,@RequestBody UserReq.UserPassword userPassword){
        boolean check = userService.checkPassword(user,userPassword);
        if(!check) throw new BadRequestException(NOT_CORRECT_PASSWORD);
        return CommonResponse.onSuccess("μ¬μ© κ°λ₯");
    }

    @ApiOperation(value = "02-18 μ μ  νν΄ π€ FRAME SETTING 02",notes = "μ ν μ°λ ν΄μ§")
    @PatchMapping("/active")
    public CommonResponse<String> unActiveUser(@AuthenticationPrincipal User user){
        userService.unActiveUser(user);
        userService.unActiveReview(user);
        return CommonResponse.onSuccess("νμ νν΄ μλ£");
    }


    @ApiOperation(value = "02-19 μ νμ© μ μ  νν΄ π€ FRAME SETTING 02",notes = "μ ν μ°λ ν΄μ§")
    @PatchMapping("/apple/active")
    public CommonResponse<String> unActiveAppleUser(@AuthenticationPrincipal User user,@RequestBody UserReq.AppleCode appleCode ) throws IOException {
        userService.unActiveUser(user);
        userService.unActiveReview(user);
        authService.revoke(appleCode.getCode());
        return CommonResponse.onSuccess("νμ νν΄ μλ£");
    }
}
