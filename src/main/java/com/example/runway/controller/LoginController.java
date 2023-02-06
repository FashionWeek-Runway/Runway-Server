package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
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

    @RequestMapping(value = "/signup", consumes = {"multipart/form-data"},method = RequestMethod.POST)
    @ApiOperation(value = "01 - 01 회원가입 🔑", notes = "회원가입 API 보내실 때 multipart/from-data 로 보내주시면 됩니다.")
    public CommonResponse<UserRes.SignUp> signup(@ModelAttribute UserReq.SignupUser signupUser) throws IOException {
        log.info("post-signup");
        log.info("api = signup ");
        try {
            if(signupUser.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
            if(signupUser.getPassword()==null) throw new BadRequestException(USERS_EMPTY_USER_PASSWORD);
            if(signupUser.getPhone()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
            if(logInService.checkuserId(signupUser.getPhone()))  throw new ForbiddenException(USERS_EXISTS_ID);
            if(logInService.checkNickName(signupUser.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);
            if(!logInService.validationPhoneNumber(signupUser.getPhone())) throw new ForbiddenException(NOT_CORRECT_PHONE_NUMBER_FORM);


            UserRes.SignUp signUp = logInService.signUp(signupUser.getMultipartFile(),signupUser);

            return new CommonResponse<>(signUp);
        }catch(BaseException e){
            return new CommonResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "01 - 02 로그인 🔑", notes = "로그인을 하는 API")
    @PostMapping("")
    public CommonResponse<UserRes.Token> login( @Valid @RequestBody UserReq.LoginUserInfo loginUserInfo){
        log.info("post-logIn");
        log.info("api = logIn ,username={}",loginUserInfo.getPhone());
        log.info("login");

        if(loginUserInfo.getPhone()==null) return new CommonResponse<>(USERS_EMPTY_USER_ID);

        if(loginUserInfo.getPassword()==null) return new CommonResponse<>(USERS_EMPTY_USER_PASSWORD);

        try {
            UserRes.Token token = logInService.logIn(loginUserInfo);
            return new CommonResponse<>(token);
        }catch (BaseException e){
            return new CommonResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "01 - 03 닉네임 중복체크 🔑", notes = "닉네임 중복체크")
    @GetMapping("/check/nickname")
    public CommonResponse<String> checkNickName(@RequestParam("nickname") String nickName) {
        log.info("get-check-nickname");
        log.info("api = check-nickname, nickname={}",nickName);
        String result="";
        if(logInService.checkNickName(nickName)){
            return new CommonResponse<>(USERS_EXISTS_NICKNAME);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }


    @ApiOperation(value = "01 - 04 유저 전화번호 중복체크 🔑", notes = "유저 아이디 중복체크")
    @GetMapping("/check/phone")
    public CommonResponse<String> checkuserId(@RequestParam("phone") String phone){
        log.info("get-check-phone");
        log.info("api = check-phone, phonenumber={}",phone);
        String result="";
        if(logInService.checkuserId(phone)){
            return new CommonResponse<>(USERS_EXISTS_ID);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }


}
