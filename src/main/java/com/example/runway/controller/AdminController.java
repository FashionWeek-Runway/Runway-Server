package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.admin.AdminReq;
import com.example.runway.service.instagram.InstagramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
@Api(tags = "06-관리자 기능")
public class AdminController {
    private final InstagramService instagramService;


    @Operation(summary = "06-01 인스타그램 피드 올리기")
    @PostMapping(value = "", consumes = {"multipart/form-data"}, produces = "application/json")
    public CommonResponse<String> postFeed(
            @AuthenticationPrincipal User user,
            @Parameter(description = "인스타 피드 종류",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart("feed") AdminReq.PostFeed postFeed,
            @RequestPart("feedImg") List<MultipartFile> feedImg
            ){
        log.info("instagramFeed Upload");

        instagramService.postFeed(postFeed,feedImg);
        return CommonResponse.onSuccess("성공");
    }

}
