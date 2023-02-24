package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.service.util.AwsS3Service;
import com.example.runway.service.util.CrawlingService;
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
    private final CrawlingService crawlingService;


    @GetMapping("/user")
    public CommonResponse<String> test(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        System.out.println("유저 아이디값:" + userId);
        return CommonResponse.onSuccess("유저 아이디값:" + userId);

    }

    @PostMapping("/img")
    public CommonResponse<String> imgUpload(@RequestParam("img") MultipartFile multipartFile) throws IOException {

        String imgurl = awsS3Service.upload(multipartFile, "store");
        return CommonResponse.onSuccess(imgurl);

    }






}
