package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.constants.Constants;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.exception.NotFoundException;
import com.example.runway.service.AuthService;
import com.example.runway.service.LoginService;
import com.example.runway.service.RedisService;
import com.example.runway.service.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.example.runway.constants.CommonResponseStatus.*;
import static com.example.runway.constants.CommonResponseStatus.USERS_EXISTS_ID;

@Api(tags = "01-로그인 🔑")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {
    private final LoginService logInService;
    private final AuthService authService;
    private final SmsService smsService;
    private final RedisService redisService;

    @RequestMapping(value = "/signup", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    @ApiOperation(value = "01-01 회원가입 🔑", notes = "회원가입 API 보내실 때 multipart/from-data 로 보내주시면 됩니다.")
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


    @ApiOperation(value = "01-02 로그인 🔑", notes = "로그인을 하는 API")
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


    @ApiOperation(value = "01-03 닉네임 중복체크 🔑", notes = "닉네임 중복체크")
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


    @ApiOperation(value = "01-04 유저 전화번호 중복체크 🔑", notes = "유저 아이디 중복체크")
    @GetMapping("/check/phone")
    public CommonResponse<String> checkuserId(@RequestParam("phone") String phone){
        log.info("get-check-phone");
        log.info("api = check-phone, phonenumber={}",phone);
        if(logInService.checkuserId(phone)) throw new BaseException(USERS_EXISTS_ID);


        return CommonResponse.onSuccess("사용 가능합니다.");

    }


    @ApiOperation(value = "01-05 유저 비밀번호 변경 🔑", notes = "유저 비밀번호찾기")
    @PostMapping("/phone")
    public CommonResponse<String> findPassword(@RequestBody UserReq.PostPassword postPassword){
        log.info("change-password-phone");
        log.info("api = check-phone, phonenumber={}",postPassword.getPassword());

        if (logInService.checkuserId(postPassword.getPhone())) throw new NotFoundException(NOT_EXIST_USER);

        logInService.modifyPassword(postPassword);
        return CommonResponse.onSuccess("비밀번호 변경성공");


    }

    @ApiOperation(value = "01-06 유저 인증번호 전송 🔑", notes = "유저 전화번호 인증")
    @PostMapping("/send")
    public CommonResponse<String> sendSMS(@RequestBody UserReq.Message message) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        log.info("send-sms");
        log.info("api = send-sms, phonenumber={}",message.getTo());

        if(logInService.checkuserId(message.getTo()))throw new BadRequestException(USERS_EXISTS_ID);
        if(!logInService.validationPhoneNumber(message.getTo())) throw new ForbiddenException(NOT_CORRECT_PHONE_NUMBER_FORM);

        if(smsService.checkLimitCertification(message.getTo())>=5)throw new BadRequestException(LIMIT_CERTIFICATE_SMS);

        UserRes.SmsResponse response=smsService.sendSms(message);
        //문자 인증 횟수 카운트
        logInService.countUserPhone(message.getTo());

        return CommonResponse.onSuccess("전송 성공");

    }
    @ApiOperation(value = "01-07 유저 인증번호 확인 🔑", notes = "유저 인증번호 확인")
    @PostMapping("/check")
    public CommonResponse<String> checkSMS(@RequestBody UserReq.MessageCheck message)  {
        log.info("send-sms");
        log.info("api = check-sms, phonenumber={}",message.getTo());
        String confirmNum=smsService.getSmsConfirmNum(message.getTo());

        if(confirmNum==null)throw new BadRequestException(NOT_EXIST_CONFIRM_NUM);

        if(confirmNum.equals(message.getConfirmNum()))return CommonResponse.onSuccess("인증 번호가 맞습니다.");

        else throw new BadRequestException(NOT_CORRECT_CONFIRM_NUM);
        //문자 인증 횟수 카운트


    }



    @ApiOperation(value = "01-08 카카오 로그인 테스트용 코드발급 🔑", notes = "유저 카카오 로그인")
    @GetMapping("/kakao")
    public CommonResponse<String> getAccessTokenKakao(@RequestParam String code){
        String accessToken=authService.getKakaoAccessToken(code);
        System.out.println(accessToken);
        return CommonResponse.onSuccess(accessToken);
    }

    @ApiOperation(value = "01-08 카카오 로그인 🔑", notes = "유저 카카오 로그인")
    @ResponseBody
    @PostMapping("/kakao")
    public CommonResponse<UserRes.Token> kakaoLogin(@RequestBody UserReq.SocialLogin SocialLogin) throws BaseException{

            UserRes.Token tokenRes = authService.logInKakaoUser(SocialLogin);
            return CommonResponse.onSuccess(tokenRes);

    }


    @ApiOperation(value = "01-09 애플 로그인 🔑", notes = "유저 애플 로그인")
    @ResponseBody
    @PostMapping("/apple")
    public CommonResponse<UserRes.Token> appleLogin(@RequestBody UserReq.SocialLogin SocialLogin) throws BaseException{

        UserRes.Token tokenRes = authService.appleLogin(SocialLogin);
        return CommonResponse.onSuccess(tokenRes);

    }




    @ApiOperation(value = "01-10 소셜 회원가입 🔑", notes = "유저 카카오 로그인")
    @ResponseBody
    @RequestMapping(value = "/signup/kakao", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    public CommonResponse<UserRes.SignUp> socialSignUp(@ModelAttribute UserReq.SocialSignUp socialSignUp) throws BaseException, IOException {

        if(socialSignUp.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
        if(socialSignUp.getSocialId()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
        if(logInService.checkuserId(socialSignUp.getSocialId()))  throw new ForbiddenException(USERS_EXISTS_SOCIAL_ID);
        if(logInService.checkNickName(socialSignUp.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);

        UserRes.SignUp signUp = null;
        if(socialSignUp.getType().equals(Constants.kakao)){
            signUp=logInService.signUpSocial(socialSignUp);
        }

        return CommonResponse.onSuccess(signUp);

    }
}
