package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.service.MapService;
import com.example.runway.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "04-맵 🗺")
@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {
    private final MapService mapService;
    private final StoreService storeService;


    //TODO 거리순으로 조회시켜주기
    @ApiOperation(value = "04-01 메인 지도 조회 + 필터링 조회 🗺 API ", notes = "04-02 와 함께 조회해야합니다 지도 조회 API ArrayList 에 아무것도 입력 안할 시 전체 조회. example = category=[]")
    @PostMapping("/filter")
    private CommonResponse<List<MapRes.Map>> getMapFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap){
        Long userId=user.getId();
        List<MapRes.Map> mapList=mapService.getMapFilter(userId,filterMap);
        return CommonResponse.onSuccess(mapList);
    }

    @ApiOperation(value = "04-02 스와이프 쇼룸 필터링 조회 🗺 API", notes = "하단 바 쇼룸 필터링 조회 API ArrayList 에 아무것도 입력 안할 시 전체 조회")
    @PostMapping("/info")
    private CommonResponse<PageResponse<List<MapRes.StoreInfo>>> getStoreInfoFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap,
                                                                                              @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                              @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) Integer size){
        Long userId=user.getId();
        PageResponse<List<MapRes.StoreInfo>> storeInfoList=mapService.getStoreInfoFilter(userId,filterMap,page,size);
        return CommonResponse.onSuccess(storeInfoList);
    }

    @ApiOperation(value = "04-03 지도 쇼룸 검색 지도 조회 마커용 🗺 검색용", notes = "지도 필터링 조회")
    @PostMapping("/search")
    private CommonResponse<List<MapRes.StoreSearchList>> getContentsBySearch(@AuthenticationPrincipal User user, @Parameter(description = "검색어", example = "0") @RequestParam(required = true) String content,
                                                                          @RequestBody MapReq.SearchStore searchStore) {
        List<MapRes.StoreSearchList> storeSearchList=mapService.getStoreBySearch(content,searchStore);
        return CommonResponse.onSuccess(storeSearchList);
    }


    @ApiOperation(value = "04-04 지도 쇼룸 검색 지도 조회 마커용 🗺 API", notes = "지도 필터링 조회")
    @PostMapping("/search")
    private CommonResponse<List<MapRes.StoreSearchList>> getStoreBySearch(@AuthenticationPrincipal User user, @Parameter(description = "검색어", example = "0") @RequestParam(required = true) String content,
                                                                          @RequestBody MapReq.SearchStore searchStore) {
        List<MapRes.StoreSearchList> storeSearchList=mapService.getStoreBySearch(content,searchStore);
        return CommonResponse.onSuccess(storeSearchList);
    }



}
