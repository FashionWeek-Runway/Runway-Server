package com.example.imenu_spring_project.controller;

import com.example.imenu_spring_project.common.CommonException;
import com.example.imenu_spring_project.common.CommonResponse;
import com.example.imenu_spring_project.dto.UserReq;
import com.example.imenu_spring_project.dto.UserRes;
import com.example.imenu_spring_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.imenu_spring_project.common.CommonResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    @ApiOperation(value = "로그인", notes = "로그인")
    @PostMapping("/login")
    public CommonResponse<UserRes.Token> login(@Valid @RequestBody UserReq.LoginUserInfo loginUserInfo){
        if(loginUserInfo.getUsername()==null){
            return new CommonResponse<>(USERS_EMPTY_USER_ID);
        }
        if(loginUserInfo.getPassword()==null){
            return new CommonResponse<>(USERS_EMPTY_USER_PASSWORD);
        }

        try {
            UserRes.Token token = userService.logIn(loginUserInfo);
            return new CommonResponse<>(token);
        }catch (CommonException e){
            return new CommonResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping("/signup")
    public CommonResponse<UserRes.Token> signup(@Valid @RequestBody UserReq.SignupUser signupUser) {
        try {
            if (userService.checkUserId(signupUser.getUsername())) { //아이디 중복 여부
                return new CommonResponse<>(USERS_EXISTS_ID);
            }
            if(!userService.validationPassword(signupUser.getPassword())){ //비밀번호 양식 체크
                return new CommonResponse<>(NOT_CORRECT_PASSWORD_FORM);
            }
            if (userService.checkNickName(signupUser.getNickname())) { //닉네임 중복 여부
                return new CommonResponse<>(USERS_EXISTS_NICKNAME);
            }
            if(!userService.validationPhoneNumber(signupUser.getPhone())){ //전화번호 양식 체크
                return new CommonResponse<>(NOT_CORRECT_PHONE_NUMBER_FORM);
            }

            UserRes.Token token = userService.signUp(signupUser);

            return new CommonResponse<>(token);
        }catch(CommonException e){
            return new CommonResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크")
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

    @ApiOperation(value = "유저 아이디 중복체크", notes = "유저 아이디 중복체크")
    @GetMapping("/check/username")
    public CommonResponse<String> checkUserId(@RequestParam("username") String username){
        String result="";
        if(userService.checkUserId(username)){
            return new CommonResponse<>(USERS_EXISTS_ID);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }
}
