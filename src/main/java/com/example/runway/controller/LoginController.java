package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.exception.NotFoundException;
import com.example.runway.jwt.TokenProvider;
import com.example.runway.service.user.AuthService;
import com.example.runway.service.user.LoginService;
import com.example.runway.service.util.RedisService;
import com.example.runway.service.user.SmsService;
import com.example.runway.util.ValidationRegex;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
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

@Api(tags = "01-λ‘κ·ΈμΈ π")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {
    private final LoginService logInService;
    private final AuthService authService;
    private final SmsService smsService;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;

    @RequestMapping(value = "/signup", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    @ApiOperation(value = "01-01 νμκ°μ π", notes = "νμκ°μ API λ³΄λ΄μ€ λ multipart/from-data λ‘ λ³΄λ΄μ£Όμλ©΄ λ©λλ€.")
    public CommonResponse<UserRes.SignUp> signup(@ModelAttribute UserReq.SignupUser signupUser) throws IOException {
        log.info("post-signup");
        log.info("api = signup 01-01");

        System.out.println(signupUser.getCategoryList());

        if(signupUser.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
        if(signupUser.getPassword()==null) throw new BadRequestException(USERS_EMPTY_USER_PASSWORD);
        if(signupUser.getPhone()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
        if(logInService.checkuserId(signupUser.getPhone()))  throw new ForbiddenException(USERS_EXISTS_ID);
        if(logInService.checkNickName(signupUser.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);
        if(!ValidationRegex.validationPhoneNumber(signupUser.getPhone())) throw new ForbiddenException(NOT_CORRECT_PHONE_NUMBER_FORM);


        UserRes.SignUp signUp = logInService.signUp(signupUser.getMultipartFile(),signupUser);

        return CommonResponse.onSuccess(signUp);

    }


    @ApiOperation(value = "01-02 λ‘κ·ΈμΈ π", notes = "λ‘κ·ΈμΈμ νλ API")
    @PostMapping("")
    public CommonResponse<UserRes.Token> login( @Valid @RequestBody UserReq.LoginUserInfo loginUserInfo){
        log.info("post-logIn");
        log.info("api = logIn 01-02 ,username={}",loginUserInfo.getPhone());

        if(loginUserInfo.getPhone()==null) throw new BaseException(USERS_EMPTY_USER_ID);

        if(loginUserInfo.getPassword()==null)throw new BaseException(USERS_EMPTY_USER_PASSWORD);

        UserRes.Token token = logInService.logIn(loginUserInfo);
        return CommonResponse.onSuccess(token);

    }


    @ApiOperation(value = "01-03 λλ€μ μ€λ³΅μ²΄ν¬ π", notes = "λλ€μ μ€λ³΅μ²΄ν¬")
    @GetMapping("/check/nickname")
    public CommonResponse<String> checkNickName(@RequestParam("nickname") String nickName) {
        log.info("get-check-nickname");
        log.info("api = check-nickname 01-03, nickname={}",nickName);
        String result="";
        if(logInService.checkNickName(nickName)){
            throw new BaseException(USERS_EXISTS_NICKNAME);
        }

        return CommonResponse.onSuccess("μ¬μ© κ°λ₯ ν©λλ€.");

    }


    @ApiOperation(value = "01-04 μ μ  μ νλ²νΈ μ€λ³΅μ²΄ν¬ π", notes = "μ μ  μμ΄λ μ€λ³΅μ²΄ν¬")
    @GetMapping("/check/phone")
    public CommonResponse<String> checkuserId(@RequestParam("phone") String phone){
        log.info("get-check-phone");
        log.info("api = check-phone 01-04, phonenumber={}",phone);
        if(logInService.checkuserId(phone)) throw new BaseException(USERS_EXISTS_ID);


        return CommonResponse.onSuccess("μ¬μ© κ°λ₯ν©λλ€.");

    }


    @ApiOperation(value = "01-05 μ μ  λΉλ°λ²νΈ λ³κ²½ π", notes = "μ μ  λΉλ°λ²νΈμ°ΎκΈ°")
    @PostMapping("/phone")
    public CommonResponse<String> findPassword(@RequestBody UserReq.PostPassword postPassword){
        log.info("change-password-phone");
        log.info("api = check-phone 01-05, phonenumber={}",postPassword.getPassword());

        if (!logInService.checkuserId(postPassword.getPhone())) throw new NotFoundException(NOT_EXIST_USER);

        logInService.modifyPassword(postPassword);
        return CommonResponse.onSuccess("λΉλ°λ²νΈ λ³κ²½μ±κ³΅");


    }

    @ApiOperation(value = "01-06 μ μ  μΈμ¦λ²νΈ μ μ‘ π", notes = "μ μ  μ νλ²νΈ μΈμ¦")
    @PostMapping("/send")
    public CommonResponse<String> sendSMS(@RequestBody UserReq.Message message) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        log.info("send-sms");
        log.info("api = send-sms 01-06, phonenumber={}",message.getTo());

        if(!ValidationRegex.validationPhoneNumber(message.getTo())) throw new ForbiddenException(NOT_CORRECT_PHONE_NUMBER_FORM);

        if(smsService.checkLimitCertification(message.getTo())>=5)throw new BadRequestException(LIMIT_CERTIFICATE_SMS);

        UserRes.SmsResponse response=smsService.sendSms(message);
        //λ¬Έμ μΈμ¦ νμ μΉ΄μ΄νΈ
        logInService.countUserPhone(message.getTo());

        return CommonResponse.onSuccess("μ μ‘ μ±κ³΅");

    }
    @ApiOperation(value = "01-07 μ μ  μΈμ¦λ²νΈ νμΈ π", notes = "μ μ  μΈμ¦λ²νΈ νμΈ")
    @PostMapping("/check")
    public CommonResponse<String> checkSMS(@RequestBody UserReq.MessageCheck message)  {
        log.info("send-sms");
        log.info("api = check-sms 01-07, phonenumber={}",message.getTo());
        String confirmNum=smsService.getSmsConfirmNum(message.getTo());

        if(confirmNum==null)throw new BadRequestException(NOT_EXIST_CONFIRM_NUM);

        if(confirmNum.equals(message.getConfirmNum()))return CommonResponse.onSuccess("μΈμ¦ λ²νΈκ° λ§μ΅λλ€.");

        else throw new BadRequestException(NOT_CORRECT_CONFIRM_NUM);
        //λ¬Έμ μΈμ¦ νμ μΉ΄μ΄νΈ


    }



    @ApiOperation(value = "01-08 μΉ΄μΉ΄μ€ λ‘κ·ΈμΈ νμ€νΈμ© μ½λλ°κΈ π", notes = "μ μ  μΉ΄μΉ΄μ€ λ‘κ·ΈμΈ")
    @GetMapping("/kakao")
    public CommonResponse<String> getAccessTokenKakao(@RequestParam String code){
        String accessToken=authService.getKakaoAccessToken(code);
        System.out.println(accessToken);
        return CommonResponse.onSuccess(accessToken);
    }

    @ApiOperation(value = "01-08 μΉ΄μΉ΄μ€ λ‘κ·ΈμΈ π", notes = "μ μ  μΉ΄μΉ΄μ€ λ‘κ·ΈμΈ")
    @ResponseBody
    @PostMapping("/kakao")
    public CommonResponse<UserRes.Token> kakaoLogin(@RequestBody UserReq.SocialLogin SocialLogin) throws BaseException{
        log.info("kakao-login");
        log.info("api = kakao-login 01-08, kakaoAccesstoken={}",SocialLogin.getAccessToken());

        UserRes.Token tokenRes = authService.logInKakaoUser(SocialLogin);
        return CommonResponse.onSuccess(tokenRes);

    }


    @ApiOperation(value = "01-09 μ ν λ‘κ·ΈμΈ π", notes = "μ μ  μ ν λ‘κ·ΈμΈ")
    @ResponseBody
    @PostMapping("/apple")
    public CommonResponse<UserRes.AppleLogin> appleLogin(@RequestBody UserReq.SocialLogin SocialLogin) throws BaseException{
        log.info("apple-login");
        log.info("api = apple-login 01-09, kakaoAccesstoken={}",SocialLogin.getAccessToken());

        UserRes.AppleLogin tokenRes = authService.appleLogin(SocialLogin);
        return CommonResponse.onSuccess(tokenRes);

    }




    @ApiOperation(value = "01-10 μμ νμκ°μ π", notes = "μ μ  μμνμκ°μ")
    @ResponseBody
    @RequestMapping(value = "/signup/kakao", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    public CommonResponse<UserRes.SignUp> socialSignUp(@ModelAttribute UserReq.SocialSignUp socialSignUp) throws BaseException, IOException {
        log.info("post-social-signup");
        log.info("api = social-signup 01-10");

        if(socialSignUp.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
        if(socialSignUp.getSocialId()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
        if(logInService.checkuserId(socialSignUp.getSocialId()))  throw new ForbiddenException(USERS_EXISTS_SOCIAL_ID);
        if(logInService.checkNickName(socialSignUp.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);

        UserRes.SignUp signUp = logInService.signUpSocial(socialSignUp);


        return CommonResponse.onSuccess(signUp);

    }

    @Operation(summary = "01-11 λ¦¬νλ μ¬ ν ν° μ ν¨μ± κ²μ¦ π", description = "μ²μ μ± μ§μμ μ²΄ν¬ API")
    @ApiImplicitParam(name="X-REFRESH-TOKEN",value = "λ¦¬νλ μ¬ ν ν°κ°",dataType = "String",paramType = "header")
    @ResponseBody
    @PostMapping("/refresh")
    public CommonResponse<UserRes.Token> checkRefreshToken(){
        log.info("check-refresh-token");
        log.info("api = check-refresh-token 01-11");

        String refreshToken=tokenProvider.getRefreshToken();

        Long userId = tokenProvider.getUserIdByRefreshToken(refreshToken);

        String redisRT= redisService.getValues(String.valueOf(userId));

        if(redisRT==null){
            throw new BadRequestException(INVALID_REFRESH_TOKEN);

        }

        UserRes.GenerateToken token=logInService.createToken(userId);

        return CommonResponse.onSuccess(new UserRes.Token(userId,token.getAccessToken(),token.getRefreshToken()));
    }


}
