package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;
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
@Api(tags = "05-ํ ๐ ")
public class HomeController {
    private final StoreService storeService;
    private final UserService userService;
    private final ReviewService reviewService;

    @ApiOperation(value = "05-02 ํํ๋ฉด ์นดํ๊ณ ๋ฆฌ ์ ํ ๐  API FRAME HOME_01", notes = "")
    @PatchMapping("/categories")
    public CommonResponse<String> postUserCategory(@AuthenticationPrincipal User user, @RequestBody HomeReq.PostUserCategory postUserCategory){
        log.info("patch-category");
        log.info("api = patch-category 05-02 ,categoryList={}", postUserCategory.getCategoryList());
        Long userId = user.getId();
        userService.postUserCategory(userId,postUserCategory);
        return CommonResponse.onSuccess("์นดํ๊ณ ๋ฆฌ ๋ณ๊ฒฝ ์ฑ๊ณต 05-03 API ๋ค์ ํธ์ถ");
    }

    @ApiOperation(value = "05-01 ํํ๋ฉด ์นดํ๊ณ ๋ฆฌ ์กฐํ ๐  API FRAME HOME_01", notes = "")
    @GetMapping("/categories")
    public CommonResponse<List<String>> getUserCategories(@AuthenticationPrincipal User user){

        log.info("get-category");
        log.info("api = get-category 05-01");

        Long userId = user.getId();

        List<String> userCategoryList=userService.getCategoryList(userId);

        return CommonResponse.onSuccess(userCategoryList);
    }

    @ApiOperation(value = "05-03 ํํ๋ฉด ์ผ๋ฃธ ์กฐํ ๐  API FRAME HOME_01,CATEGORY_01", notes = "์นดํ๊ณ ๋ฆฌ ๊ธฐ๋ฐ์ผ๋ก ์ผ๋ฃธ์ ๋ณด์ฌ์ฃผ๋ API ์นดํ๊ณ ๋ฆฌ ์ฌ์ ํ์ ํด๋น API ๋ก ์ฌ์กฐํ ํ๋ฉด ๋ฉ๋๋ค.")
    @GetMapping("")
    public CommonResponse<List<HomeRes.StoreInfo>> getStoreInfo(@AuthenticationPrincipal User user,@Parameter(description = "ํํ๋ฉด ์กฐํ ์ 0 ์ ์ฒด๋ณด๊ธฐ ์กฐํ ์ 1 ์๋๋ค. ํํ๋ฉด์ 10๊ฐ, ์ ์ฒด๋ 30๊ฐ ์๋๋ค", example = "0") @RequestParam(required = true) Integer type){
        log.info("get-recommend-store");
        log.info("api = get-recommend-store 05-03");

        Long userId = user.getId();

        List<HomeRes.StoreInfo> storeInfo=storeService.recommendStore(userId,type);
        return CommonResponse.onSuccess(storeInfo);
    }


    @ApiOperation(value = "05-04 ํํ๋ฉด ๋ฆฌ๋ทฐ ์กฐํ ๐  API FRAME HOME_01", notes = "์นดํ๊ณ ๋ฆฌ ๊ธฐ๋ฐ์ผ๋ก ์ผ๋ฃธ์ ๋ณด์ฌ์ฃผ๋ API ์นดํ๊ณ ๋ฆฌ ์ฌ์ ํ์ ํด๋น API ๋ก ์ฌ์กฐํ ํ๋ฉด ๋ฉ๋๋ค.")
    @GetMapping("/review")
    public CommonResponse<PageResponse<List<HomeRes.Review>>> getReviewList(@AuthenticationPrincipal User user,
                                                                           @Parameter(description = "ํ์ด์ง", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                           @Parameter(description = "ํ์ด์ง ์ฌ์ด์ฆ", example = "10") @RequestParam(required = true)  Integer size){
        log.info("get-recommend-review");
        log.info("api = get-recommend-review 05-04");

        Long userId = user.getId();
        PageResponse<List<HomeRes.Review>> review= reviewService.recommendReview(userId,page,size);

       return CommonResponse.onSuccess(review);
    }

    @ApiOperation(value = "05-05 ํํ๋ฉด ๋ฆฌ๋ทฐ ์์ธ ์กฐํ ๐  API FRAME HOME_01", notes = "๋ฆฌ๋ทฐ ์์ธ ์กฐํ")
    @GetMapping("/review/detail/{reviewId}")
    public CommonResponse<HomeRes.ReviewInfo> getReviewDetail(@AuthenticationPrincipal User user,
                                                              @Parameter(description = "review ๋ฆฌ๋ทฐ Id๊ฐ") @PathVariable Long reviewId){
        log.info("get-recommend-review");
        log.info("api = get-recommend-review-detail 05-05");

        Long userId = user.getId();

        HomeRes.ReviewInfo review = reviewService.getRecommendedReview(userId,reviewId);
        reviewService.readReview(reviewId,userId);
        return CommonResponse.onSuccess(review);
    }





}