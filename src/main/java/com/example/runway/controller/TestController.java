package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.exception.BaseException;
import com.example.runway.exception.ForbiddenException;
import com.example.runway.exception.NotFoundException;
import com.example.runway.service.AwsS3Service;
import com.example.runway.service.CrawlingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_STORE;


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

        String imgurl = awsS3Service.upload(multipartFile, "test");
        return CommonResponse.onSuccess(imgurl);

    }






}
