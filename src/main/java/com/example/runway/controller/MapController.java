package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.exception.BadRequestException;
import com.example.runway.service.MapService;
import com.example.runway.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_STORE;

@Api(tags = "04-맵 🗺")
@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {
    private final MapService mapService;
    private final StoreService storeService;


    @ApiOperation(value = "04-01 메인 지도 조회 + 필터링 조회 🗺 API ", notes = "지도 조회 API ArrayList 에 아무것도 입력 안할 시 전체 조회. example = category=[]")
    @PostMapping("/filter")
    private CommonResponse<List<MapRes.GetMapRes>> getMapFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap){
        Long userId=user.getId();
        List<MapRes.GetMapRes> mapList=mapService.getMapFilter(userId,filterMap);
        return CommonResponse.onSuccess(mapList);
    }

    @ApiOperation(value = "04-02 지도 쇼룸 필터링 조회 🗺 API", notes = "지도 필터링 조회")
    @PostMapping("/info")
    private CommonResponse<List<MapRes.GetStoreInfoListRes>> getStoreInfoFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap){
        Long userId=user.getId();
        List<MapRes.GetStoreInfoListRes> storeInfoList=mapService.getStoreInfoFilter(userId,filterMap);
        return CommonResponse.onSuccess(storeInfoList);
    }

    @ApiOperation(value = "04-03  Map 쇼룸 상세 페이지 상단 정보 🗺 API",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/detail/{storeId}")
    private CommonResponse<StoreRes.StoreInfo> getStoreDetail(@AuthenticationPrincipal User user,@PathVariable("storeId") Long storeId){
        if(!storeService.checkStore(storeId))throw new BadRequestException(NOT_EXIST_STORE);
        StoreRes.StoreInfo storeInfo=storeService.getStoreDetail(user,storeId);
        return CommonResponse.onSuccess(storeInfo);
    }

    @ApiOperation(value = "04-04 Map 쇼룸 사용자 후기  🗺 API",notes = "지도에서 가게 상세 조회 API")
    @GetMapping("/review/{storeId}")
    private CommonResponse<List<StoreRes.StoreReview>> getStoreReview(@AuthenticationPrincipal User user, @PathVariable("storeId") Long storeId,
                                                                      @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                      @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) @Min(value = 10) @Max(value = 50) Integer size
    ){
        if(!storeService.checkStore(storeId))throw new BadRequestException(NOT_EXIST_STORE);
        List<StoreRes.StoreReview> storeReview=storeService.getStoreReview(storeId,page);

        return CommonResponse.onSuccess(storeReview);
    }

}
