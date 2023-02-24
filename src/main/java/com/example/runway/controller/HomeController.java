package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.service.store.StoreService;
import com.example.runway.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Api(tags = "05-홈 🏠")
public class HomeController {
    private final StoreService storeService;
    private final UserService userService;

    @ApiOperation(value = "05-02 홈화면 카테고리 선택 🏠 API FRAME HOME_01", notes = "")
    @PatchMapping("/categories")
    public CommonResponse<String> postUserCategory(@AuthenticationPrincipal User user, @RequestBody HomeReq.PostUserCategory postUserCategory){
        Long userId = user.getId();
        userService.postUserCategory(userId,postUserCategory);
        return CommonResponse.onSuccess("카테고리 변경 성공 05-03 API 다시 호출");
    }

    @ApiOperation(value = "05-01 홈화면 카테고리 조회 🏠 API FRAME HOME_01", notes = "")
    @GetMapping("/categories")
    public CommonResponse<List<String>> getUserCategories(@AuthenticationPrincipal User user){
        Long userId = user.getId();

        List<String> userCategoryList=storeService.getCategoryList(userId);

        return CommonResponse.onSuccess(userCategoryList);
    }

    @ApiOperation(value = "05-03 홈화면 쇼룸 조회 🏠 API FRAME HOME_01", notes = "카테고리 기반으로 쇼룸을 보여주는 API 카테고리 재선택시 해당 API 로 재조회 하면 됩니다.")
    @GetMapping("")
    public CommonResponse<List<HomeRes.StoreInfo>> getStoreInfo(@AuthenticationPrincipal User user){
        Long userId = user.getId();
        List<HomeRes.StoreInfo> storeInfo=storeService.recommendStore(userId);
        return CommonResponse.onSuccess(storeInfo);
    }
}