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
    public CommonResponse<String> test(@RequestParam("img") String img) {
        awsS3Service.deleteImage(img);
        return CommonResponse.onSuccess("삭제");

    }

    @PostMapping("/img")
    public CommonResponse<String> imgUpload(@RequestParam("img") MultipartFile multipartFile) throws IOException {
        String imgurl = awsS3Service.upload(multipartFile, "insta");
        return CommonResponse.onSuccess(imgurl);

    }








}
