package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.exception.NotFoundException;
import com.example.runway.service.CrawlingService;
import com.example.runway.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_STORE;

@Api(tags = "03-쇼룸🏬")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;
    private final CrawlingService crawlingService;

    @ApiOperation(value = "03-01 쇼룸 홈화면 조회🏬API",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("")
    private CommonResponse<StoreRes.HomeList> getMainHome(@AuthenticationPrincipal User user){
        Long userId=user.getId();
        StoreRes.HomeList HomeList=storeService.getMainHome(userId);
        return CommonResponse.onSuccess(HomeList);
    }


    @ApiOperation(value = "03-02 쇼룸 상세 페이지 상단 정보 🏬 API",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/detail/{storeId}")
    private CommonResponse<StoreRes.StoreInfo> getStoreDetail(@AuthenticationPrincipal User user,@PathVariable("storeId") Long storeId){
        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);
        StoreRes.StoreInfo storeInfo=storeService.getStoreDetail(user,storeId);
        return CommonResponse.onSuccess(storeInfo);
    }

    @ApiOperation(value = "03-03 쇼룸 사용자 후기  🏬 API",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/review/{storeId}")
    private CommonResponse<PageResponse<List<StoreRes.StoreReview>>> getStoreReview(@AuthenticationPrincipal User user, @PathVariable("storeId") Long storeId,
                                                                                    @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                    @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true)  Integer size
    ){
        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);
        PageResponse<List<StoreRes.StoreReview>> storeReview=storeService.getStoreReview(storeId,page,size);

        return CommonResponse.onSuccess(storeReview);
    }

    @ApiOperation(value = "03-04 쇼룸 웹 스크랩핑 🏬 API",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/blog/{storeId}")
    private CommonResponse<List<StoreRes.StoreBlog>> getStoreBlog(@AuthenticationPrincipal User user, @PathVariable("storeId") Long storeId,
                                                                  @Parameter(description = "매장이름", example = "0") @RequestParam(required = true) String storeName)
    {
        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);

        List<StoreRes.StoreBlog> storeBlog=crawlingService.getStoreBlog(storeName);

        return CommonResponse.onSuccess(storeBlog);
    }


    @ApiOperation(value = "03-05 쇼룸 후기작성  🏬 API",notes = "쇼룸 후기 작성 API")
    @PostMapping("/review/{storeId}")
    private CommonResponse<String> postStoreReview(@AuthenticationPrincipal User user, @PathVariable("storeId") Long storeId,
                                                   @Parameter(description="img",example ="이미지") @RequestPart(value="img",required = true) MultipartFile multipartFile) throws IOException {
        Long userId=user.getId();

        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);

        storeService.postStoreReview(storeId,userId,multipartFile);

        return CommonResponse.onSuccess("리뷰 등록 성공");
    }

    @ApiOperation(value = "03-06 쇼룸 후기 조회  🏬 API",notes = "쇼룸 후기 작성 API")
    @PostMapping("/review/{storeId}")
    private CommonResponse<String> getStoreReview(@AuthenticationPrincipal User user, @PathVariable("storeId") Long storeId,
                                                   @Parameter(description="img",example ="이미지") @RequestPart(value="img",required = true) MultipartFile multipartFile) throws IOException {
        Long userId=user.getId();

        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);

        storeService.postStoreReview(storeId,userId,multipartFile);

        return CommonResponse.onSuccess("리뷰 등록 성공");
    }



}
