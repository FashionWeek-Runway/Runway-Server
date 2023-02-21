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

    @ApiOperation(value = "03-01 쇼룸 북마크 🏬 API FRAME MAPDETAIL_01",notes = "북마크 Check,UnCheck ")
    @PostMapping("/{storeId}")
    private CommonResponse<String> bookMarkStore(@AuthenticationPrincipal User user, @Parameter(description = "storeId 쇼룸 Id값") @PathVariable("storeId") Long storeId){
        Long userId= user.getId();
        boolean checkBookmark=storeService.existsBookMark(userId,storeId);
        if(checkBookmark){
            storeService.unCheckBookMark(userId,storeId);
            return CommonResponse.onSuccess("북마크 해제 성공");
        }
        else{
            storeService.checkBookMark(userId,storeId);
            return CommonResponse.onSuccess("북마크 성공");
        }
    }
    @ApiOperation(value = "03-02 쇼룸 상세 페이지 상단 정보 🏬 API FRAME MAPDETAIL_01",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/detail/{storeId}")
    private CommonResponse<StoreRes.StoreInfo> getStoreDetail(@AuthenticationPrincipal User user,@Parameter(description = "storeId 쇼룸 Id값") @PathVariable("storeId") Long storeId){
        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);
        StoreRes.StoreInfo storeInfo=storeService.getStoreDetail(user,storeId);
        return CommonResponse.onSuccess(storeInfo);
    }

    @ApiOperation(value = "03-03 쇼룸 사용자 후기 🏬 API FRAME MAPDETAIL_01",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/review/{storeId}")
    private CommonResponse<PageResponse<List<StoreRes.StoreReview>>> getStoreReview(@AuthenticationPrincipal User user,@Parameter(description = "storeId 쇼룸 Id값") @PathVariable("storeId") Long storeId,
                                                                                    @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                    @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true)  Integer size
    ){
        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);
        PageResponse<List<StoreRes.StoreReview>> storeReview=storeService.getStoreReview(storeId,page,size);

        return CommonResponse.onSuccess(storeReview);
    }

    @ApiOperation(value = "03-04 쇼룸 웹 스크랩핑 🏬 API FRAME MAPDETAIL_01",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/blog/{storeId}")
    private CommonResponse<List<StoreRes.StoreBlog>> getStoreBlog(@AuthenticationPrincipal User user,@Parameter(description = "storeId 쇼룸 Id값") @PathVariable("storeId") Long storeId,
                                                                  @Parameter(description = "매장이름", example = "0") @RequestParam(required = true) String storeName)
    {
        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);

        List<StoreRes.StoreBlog> storeBlog=crawlingService.getStoreBlog(storeName);

        return CommonResponse.onSuccess(storeBlog);
    }


    @ApiOperation(value = "03-05 쇼룸 후기작성 🏬 API FRAME REVIEW_01",notes = "쇼룸 후기 작성 API")
    @PostMapping("/review/{storeId}")
    private CommonResponse<String> postStoreReview(@AuthenticationPrincipal User user,@Parameter(description = "storeId 쇼룸 Id값") @PathVariable("storeId") Long storeId,
                                                   @Parameter(description="img",example ="이미지") @RequestPart(value="img",required = true) MultipartFile multipartFile) throws IOException {
        Long userId=user.getId();

        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);

        storeService.postStoreReview(storeId,userId,multipartFile);

        return CommonResponse.onSuccess("리뷰 등록 성공");
    }

    @ApiOperation(value = "03-06 쇼룸 사장님 소식 리스트 조회🏬 FRAME 2608453 API",notes = "쇼룸 사장님 소식 리스트 API")
    @GetMapping("/feed/{storeId}")
    private CommonResponse<PageResponse<List<StoreRes.StoreBoardList>>> getStoreBoardList(@AuthenticationPrincipal User user,@Parameter(description = "storeId 쇼룸 Id값") @PathVariable("storeId") Long storeId,
                                                                                  @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                  @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true)  Integer size)  {
        Long userId=user.getId();

        if(!storeService.checkStore(storeId))throw new NotFoundException(NOT_EXIST_STORE);

        PageResponse<List<StoreRes.StoreBoardList>> storeBoard=storeService.getStoreBoard(userId,storeId,page,size);
        return CommonResponse.onSuccess(storeBoard);
    }


    @ApiOperation(value = "03-09 쇼룸 후기 상세 조회  🏬 API",notes = "쇼룸 후기 작성 API")
    @GetMapping("/review/detail/{reviewId}")
    private CommonResponse<StoreRes.ReviewInfo> getStoreReviewByReviewId(@AuthenticationPrincipal User user,
                                                                         @Parameter(description = "reviewId 값 보내주기", example = "0")   @PathVariable("reviewId") Long reviewId){
        Long userId=user.getId();


        StoreRes.ReviewInfo reviewInfo=storeService.getStoreReviewByReviewId(reviewId);

        return CommonResponse.onSuccess(reviewInfo);
    }




    @ApiOperation(value = "03-07 쇼룸 사장님 소식 조회 🏬 API FRAME FEED_01" ,notes = "쇼룸 사장님 소식 리스트 API")
    @GetMapping("/feed/info/{feedId}")
    private CommonResponse<StoreRes.StoreBoard> getStoreBoard(@AuthenticationPrincipal User user, @Parameter(description = "feedId 소식 Id값") @PathVariable("feedId") Long feedId) {
        Long userId=user.getId();


        StoreRes.StoreBoard storeBoard=storeService.getStoreBoardById(userId,feedId);
        return CommonResponse.onSuccess(storeBoard);
    }

    @ApiOperation(value = "03-08 소식 북마크 🏬 API FRAME FEED_01",notes = "북마크 Check,UnCheck ")
    @PostMapping("/feed/{feedId}")
    private CommonResponse<String> bookMarkFeed(@AuthenticationPrincipal User user, @Parameter(description = "feedId 소식 Id값") @PathVariable("feedId") Long feedId){
        Long userId= user.getId();
        boolean checkBookmark=storeService.existsBookMarkFeed(userId,feedId);
        if(checkBookmark){
            storeService.unCheckBookMarkFeed(userId,feedId);
            return CommonResponse.onSuccess("북마크 해제 성공");
        }
        else{
            storeService.checkBookMarkFeed(userId,feedId);
            return CommonResponse.onSuccess("북마크 성공");
        }
    }


}
