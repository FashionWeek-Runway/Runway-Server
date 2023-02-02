package com.example.runway.controller;

import com.example.runway.exception.BaseException;
import com.example.runway.common.CommonResponse;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.runway.common.CommonResponseStatus.*;

@Api(tags = "01. 사용자 👤")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    @ApiResponses(
            {
                    @ApiResponse(code = 1000, message = "요청 성공."),
                    @ApiResponse(code = 2010, message = "유저 아이디 값을 입력해주세요."),
                    @ApiResponse(code = 2024, message = "존재하지 않는 유저입니다."),
                    @ApiResponse(code = 2027, message = "비밀번호가 일치하지 않습니다."),
                    @ApiResponse(code = 2028, message = "이미 탈퇴된 유저입니다.")
            }
    )
    @ApiOperation(value = "로그인 👤", notes = "로그인을 하는 API")
    @PostMapping("/login")
    public CommonResponse<UserRes.Token> login(@Valid @RequestBody UserReq.LoginUserInfo loginUserInfo){
        log.info("login");
        if(loginUserInfo.getPhone()==null){
            return new CommonResponse<>(USERS_EMPTY_USER_ID);
        }
        if(loginUserInfo.getPassword()==null){
            return new CommonResponse<>(USERS_EMPTY_USER_PASSWORD);
        }

        try {
            UserRes.Token token = userService.logIn(loginUserInfo);
            return new CommonResponse<>(token);
        }catch (BaseException e){
            return new CommonResponse<>(e.getStatus());
        }
    }

    @ApiResponses(
            {
                    @ApiResponse(code = 1000, message = "요청 성공."),
                    @ApiResponse(code = 2020, message = "중복된 아이디 입니다."),
                    @ApiResponse(code = 2024, message = "중복된 닉네임입니다."),
                    @ApiResponse(code = 2034, message = "전화번호를 하이픈(-) 없이 입력해주세요."),
                    @ApiResponse(code = 2035, message = "비밀번호는 영문과 특수문자를 포함하며 8자 이상이어야 합니다."),
                    @ApiResponse(code = 2034, message = "전화번호를 하이픈(-) 없이 입력해주세요.")
            }
    )
    @ApiOperation(value = "회원가입 👤", notes = "회원가입 ")
    @PostMapping("/signup")
    public CommonResponse<UserRes.Token> signup(@Valid @RequestBody UserReq.SignupUser signupUser) {
        try {
            if (userService.checkUserId(signupUser.getPhone()))  throw new ForbiddenException(USERS_EXISTS_ID);
            if(!userService.validationPassword(signupUser.getPassword())) throw new ForbiddenException(NOT_CORRECT_PASSWORD_FORM);
            if (userService.checkNickName(signupUser.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);
            if(!userService.validationPhoneNumber(signupUser.getPhone())) throw new ForbiddenException(NOT_CORRECT_PHONE_NUMBER_FORM);

            log.info("sign-up");

            UserRes.Token token = userService.signUp(signupUser);

            return new CommonResponse<>(token);
        }catch(BaseException e){
            return new CommonResponse<>(e.getStatus());
        }
    }

    @ApiResponses(
            {
                    @ApiResponse(code = 1000, message = "요청 성공."),
                    @ApiResponse(code = 2024, message = "중복된 닉네임입니다.")
            }
    )
    @ApiOperation(value = "닉네임 중복체크 👤", notes = "닉네임 중복체크")
    @GetMapping("/check/nickname")
    public CommonResponse<String> checkNickName(@RequestParam("nickname") String nickName) {
        String result="";
        if(userService.checkNickName(nickName)){
            return new CommonResponse<>(USERS_EXISTS_NICKNAME);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }


    @ApiResponses(
            {
                    @ApiResponse(code = 1000, message = "요청 성공."),
                    @ApiResponse(code = 2020, message = "중복된 아이디 입니다.")
            }
    )
    @ApiOperation(value = "유저 아이디 중복체크 👤", notes = "유저 아이디 중복체크")
    @GetMapping("/check/phone")
    public CommonResponse<String> checkUserId(@RequestParam("phone") String phone){
        String result="";
        if(userService.checkUserId(phone)){
            return new CommonResponse<>(USERS_EXISTS_ID);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }
}
