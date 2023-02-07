package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.service.AuthService;
import com.example.runway.service.AwsS3Service;
import com.example.runway.service.LoginService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.io.IOException;

import static com.example.runway.common.CommonResponseStatus.*;
import static com.example.runway.common.CommonResponseStatus.USERS_EXISTS_ID;

@Api(tags = "01 - 로그인 🔑")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {
    private final LoginService logInService;
    private final AuthService authService;

    @RequestMapping(value = "/signup", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    @ApiOperation(value = "01 - 01 회원가입 🔑", notes = "회원가입 API 보내실 때 multipart/from-data 로 보내주시면 됩니다.")
    public CommonResponse<UserRes.SignUp> signup(@ModelAttribute UserReq.SignupUser signupUser) throws IOException {
        log.info("post-signup");
        log.info("api = signup ");
        if(signupUser.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
        if(signupUser.getPassword()==null) throw new BadRequestException(USERS_EMPTY_USER_PASSWORD);
        if(signupUser.getPhone()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
        if(logInService.checkuserId(signupUser.getPhone()))  throw new ForbiddenException(USERS_EXISTS_ID);
        if(logInService.checkNickName(signupUser.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);
        if(!logInService.validationPhoneNumber(signupUser.getPhone())) throw new ForbiddenException(NOT_CORRECT_PHONE_NUMBER_FORM);


        UserRes.SignUp signUp = logInService.signUp(signupUser.getMultipartFile(),signupUser);

        return CommonResponse.onSuccess(signUp);

    }


    @ApiOperation(value = "01 - 02 로그인 🔑", notes = "로그인을 하는 API")
    @PostMapping("")
    public CommonResponse<UserRes.Token> login( @Valid @RequestBody UserReq.LoginUserInfo loginUserInfo){
        log.info("post-logIn");
        log.info("api = logIn ,username={}",loginUserInfo.getPhone());
        log.info("login");

        if(loginUserInfo.getPhone()==null) throw new BaseException(USERS_EMPTY_USER_ID);

        if(loginUserInfo.getPassword()==null)throw new BaseException(USERS_EMPTY_USER_PASSWORD);

        UserRes.Token token = logInService.logIn(loginUserInfo);
        return CommonResponse.onSuccess(token);

    }


    @ApiOperation(value = "01 - 03 닉네임 중복체크 🔑", notes = "닉네임 중복체크")
    @GetMapping("/check/nickname")
    public CommonResponse<String> checkNickName(@RequestParam("nickname") String nickName) {
        log.info("get-check-nickname");
        log.info("api = check-nickname, nickname={}",nickName);
        String result="";
        if(logInService.checkNickName(nickName)){
            throw new BaseException(USERS_EXISTS_NICKNAME);
        }

        return CommonResponse.onSuccess("사용 가능 합니다.");

    }


    @ApiOperation(value = "01 - 04 유저 전화번호 중복체크 🔑", notes = "유저 아이디 중복체크")
    @GetMapping("/check/phone")
    public CommonResponse<String> checkuserId(@RequestParam("phone") String phone){
        log.info("get-check-phone");
        log.info("api = check-phone, phonenumber={}",phone);
        if(logInService.checkuserId(phone)) throw new BaseException(USERS_EXISTS_ID);


        return CommonResponse.onSuccess("사용 가능합니다.");

    }


    @ApiOperation(value = "01 - 05 유저 비밀번호 변경 🔑", notes = "유저 비밀번호찾기")
    @PostMapping("/phone")
    public CommonResponse<String> findPassword(@RequestBody UserReq.PostPassword postPassword){
        log.info("change-password-phone");
        log.info("api = check-phone, phonenumber={}",postPassword.getPassword());

        if (logInService.checkuserId(postPassword.getPhone())) throw new ForbiddenException(NOT_EXIST_USER);

        logInService.modifyPassword(postPassword);
        return CommonResponse.onSuccess("비밀번호 변경성공");


    }

    @ApiOperation(value = "01 - 06 유저 전화번호 인증 🔑", notes = "유저 전화번호 인증")
    @GetMapping("/send")
    public CommonResponse<String> sendSMS(@RequestParam("phone") String phone){
        log.info("send-sms");
        log.info("api = send-sms, phonenumber={}",phone);

        logInService.countUserPhone(phone);

        return CommonResponse.onSuccess("사용 가능합니다.");

    }



    @ApiOperation(value = "01 - 07 카카오 로그인 테스트용 코드발급🔑", notes = "유저 카카오 로그인")
    @GetMapping("/kakao")
    public CommonResponse<String> getAccessTokenKakao(@RequestParam String code){
        String accessToken=authService.getKakaoAccessToken(code);
        System.out.println(accessToken);
        return CommonResponse.onSuccess(accessToken);
    }

    @ApiOperation(value = "01 - 07 카카오 로그인 🔑", notes = "유저 카카오 로그인")
    @ResponseBody
    @PostMapping("/kakao")
    public CommonResponse<UserRes.Token> kakaoLogin(@RequestBody UserReq.SocialReq socialReq) throws BaseException{

            UserRes.Token tokenRes = authService.logInKakaoUser(socialReq);
            return CommonResponse.onSuccess(tokenRes);

    }

    @ApiOperation(value = "01 - 09 소셜 회원가입 🔑", notes = "유저 카카오 로그인")
    @ResponseBody
    @RequestMapping(value = "/signup/kakao", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    public CommonResponse<UserRes.SignUp> socialSignUp(@ModelAttribute UserReq.SocialSignUp socialSignUp) throws BaseException, IOException {
        if(socialSignUp.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
        if(socialSignUp.getSocialId()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
        if(logInService.checkuserId(socialSignUp.getSocialId()))  throw new ForbiddenException(USERS_EXISTS_SOCIAL_ID);
        if(logInService.checkNickName(socialSignUp.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);
        UserRes.SignUp signUp = logInService.signUpSocial(socialSignUp);
        return CommonResponse.onSuccess(signUp);

    }
}
