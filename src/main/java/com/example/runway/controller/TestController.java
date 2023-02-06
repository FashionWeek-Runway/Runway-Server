package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {
    private final AwsS3Service awsS3Service;


    @GetMapping("/user")
    public CommonResponse<String> test(@AuthenticationPrincipal User user) {
            Long userId = user.getId();
            System.out.println("유저 아이디값:" + userId);
            return new CommonResponse<>("유저 아이디값:" + userId);

    }

    @PostMapping("/img")
    public CommonResponse<String> imgUpload(@RequestParam("img")MultipartFile multipartFile) throws IOException {
        try {
            String imgurl = awsS3Service.upload(multipartFile, "test");
            return new CommonResponse<>(imgurl);
        }catch(BaseException e){
            return new CommonResponse<>(e.getStatus());
        }
        }







}
