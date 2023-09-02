package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.service.home.PopUpService;
import com.example.runway.service.instagram.InstagramService;
import com.example.runway.service.store.ReviewService;
import com.example.runway.service.store.StoreService;
import com.example.runway.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "05-홈 🏠")
public class HomeController {
    private final StoreService storeService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final InstagramService instagramService;
    private final PopUpService popUpService;

    @ApiOperation(value = "05-02 홈화면 카테고리 선택 🏠 API FRAME HOME_01", notes = "")
    @PatchMapping("/categories")
    public CommonResponse<String> postUserCategory(@AuthenticationPrincipal User user, @RequestBody HomeReq.PostUserCategory postUserCategory){
        log.info("patch-category");
        log.info("api = patch-category 05-02 ,categoryList={}", postUserCategory.getCategoryList());
        Long userId = user.getId();
        userService.postUserCategory(userId,postUserCategory);
        return CommonResponse.onSuccess("카테고리 변경 성공 05-03 API 다시 호출");
    }

    @ApiOperation(value = "05-01 홈화면 카테고리 조회 🏠 API FRAME HOME_01", notes = "")
    @GetMapping("/categories")
    public CommonResponse<List<String>> getUserCategories(@AuthenticationPrincipal User user){

        log.info("get-category");
        log.info("api = get-category 05-01");

        Long userId = user.getId();

        List<String> userCategoryList=userService.getCategoryList(userId);

        return CommonResponse.onSuccess(userCategoryList);
    }

    @ApiOperation(value = "05-03 홈화면 쇼룸 조회 🏠 API FRAME HOME_01,CATEGORY_01", notes = "카테고리 기반으로 쇼룸을 보여주는 API 카테고리 재선택시 해당 API 로 재조회 하면 됩니다.")
    @GetMapping("")
    public CommonResponse<List<HomeRes.StoreInfo>> getStoreInfo(@AuthenticationPrincipal User user,@Parameter(description = "홈화면 조회 시 0 전체보기 조회 시 1 입니다. 홈화면은 10개, 전체는 30개 입니다", example = "0") @RequestParam(required = true) Integer type){
        log.info("get-recommend-store");
        log.info("api = get-recommend-store 05-03");

        Long userId = user.getId();

        List<HomeRes.StoreInfo> storeInfo=storeService.recommendStore(userId,type);
        return CommonResponse.onSuccess(storeInfo);
    }


    @ApiOperation(value = "05-04 홈화면 리뷰 조회 🏠 API FRAME HOME_01", notes = "카테고리 기반으로 쇼룸을 보여주는 API 카테고리 재선택시 해당 API 로 재조회 하면 됩니다.")
    @GetMapping("/review")
    public CommonResponse<PageResponse<List<HomeRes.Review>>> getReviewList(@AuthenticationPrincipal User user,
                                                                           @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                           @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true)  Integer size){
        log.info("get-recommend-review");
        log.info("api = get-recommend-review 05-04");

        Long userId = user.getId();
        PageResponse<List<HomeRes.Review>> review= reviewService.recommendReview(userId,page,size);

       return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "05-05 홈화면 리뷰 상세 조회 🏠 API FRAME HOME_01", notes = "리뷰 상세 조회")
    @GetMapping("/review/detail/{reviewId}")
    public CommonResponse<HomeRes.ReviewInfo> getReviewDetail(@AuthenticationPrincipal User user,
                                                              @Parameter(description = "review 리뷰 Id값") @PathVariable Long reviewId){
        log.info("get-recommend-review");
        log.info("api = get-recommend-review-detail 05-05");

        Long userId = user.getId();

        HomeRes.ReviewInfo review = reviewService.getRecommendedReview(userId,reviewId);
        reviewService.readReview(reviewId,userId);
        return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "05-05 인스타 피드 조회",notes = "v2 인스타 조회 기능")
    @GetMapping("/insta")
    public CommonResponse<PageResponse<List<HomeRes.InstaFeed>>> getInstaFeed(@Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0") @Min(value = 0) int page,
                                                          @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  int size){
        log.info("get-insta-feed");
        log.info("api = get-intal-feed-list 05-05");
        return CommonResponse.onSuccess(instagramService.getInstaFeed(size, page));
    }

    @ApiOperation(value = "05-06 홈화면 홈 광고 조회",notes = "v2 홈화면 조회 기능")
    @GetMapping("/pop-up")
    public CommonResponse<List<HomeRes.PopUp>> getPopUp(){
        return CommonResponse.onSuccess(popUpService.getPopUp());
    }






}