package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/user")
    public CommonResponse<String> test(@AuthenticationPrincipal User user) {
            Long userId = user.getId();
            System.out.println("유저 아이디값:" + userId);
            return new CommonResponse<>("유저 아이디값:" + userId);

    }






}
