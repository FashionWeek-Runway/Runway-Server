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
    @ApiOperation(value = "04-01 메인 지도 조회 + 필터링 조회 마커용 🗺 API FRAME MAP_03,04 ", notes = "04-02 와 함께 조회해야합니다 지도 조회 API ArrayList 에 아무것도 입력 안할 시 전체 조회. example = category=[]")
    @PostMapping("/filter")
    private CommonResponse<List<MapRes.Map>> getMapFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap){
        Long userId=user.getId();
        List<MapRes.Map> mapList=mapService.getMapFilter(userId,filterMap);
        return CommonResponse.onSuccess(mapList);
    }

    @ApiOperation(value = "04-02 하단 스와이프 쇼룸 필터링 조회 🗺 API FRAME MAP_03,04", notes = "하단 바 쇼룸 필터링 조회 API ArrayList에 아무 것도 입력 안할 시 전체 조회")
    @PostMapping("/info")
    private CommonResponse<PageResponse<List<MapRes.StoreInfo>>> getStoreInfoFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap,
                                                                                              @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                              @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) Integer size){
        Long userId=user.getId();
        PageResponse<List<MapRes.StoreInfo>> storeInfoList=mapService.getStoreInfoFilter(userId,filterMap,page,size);
        return CommonResponse.onSuccess(storeInfoList);
    }

    //검색
    @ApiOperation(value = "04-03 지도 쇼룸 검색 지도 조회 검색용 🗺 API FRAME SEARCH_07", notes = "지도 검색 조회")
    @PostMapping("/search")
    private CommonResponse<MapRes.SearchList> getContentsBySearch(@AuthenticationPrincipal User user, @RequestBody MapReq.SearchStore searchStore) {
        MapRes.SearchList storeSearchList=mapService.getStoreBySearch(searchStore);
        return CommonResponse.onSuccess(storeSearchList);
    }

    @ApiOperation(value = "04-04 지도 매장 단일 선택 하단 스와이프 조회 🗺 API FRAME MAP_07", notes = "지도 필터링 조회")
    @GetMapping("/info/{storeId}")
    private CommonResponse<MapRes.StoreInfo> getStoreByStoreId(@AuthenticationPrincipal User user,@Parameter(description = "storeId 값 보내주기", example = "0")  @PathVariable Long storeId) {
        MapRes.StoreInfo storeInfo = mapService.getStoreByStoreId(storeId);
        return CommonResponse.onSuccess(storeInfo);
    }

    //지역 마커
    @ApiOperation(value = "04-05 지도 쇼룸 검색 지역 마커용 🗺 API FRAME SEARCH_01", notes = "지역에 있는 모든 쇼룸 정보")
    @GetMapping("/region/{regionId}")
    private CommonResponse<List<MapRes.MapMarkerList>> getStoreByRegion(@AuthenticationPrincipal User user, @Parameter(description = "지역 이름 보내주는 region 값 보내주기", example = "0") @PathVariable("regionId") Long regionId) {
        List<MapRes.MapMarkerList> storeSearchList=mapService.getStoreByRegion(regionId);
        return CommonResponse.onSuccess(storeSearchList);
    }


    @ApiOperation(value = "04-06 지도 쇼룸 검색 지역 스와이프용 🗺 API FRAME SEARCH_02", notes = "지도 검색 조회")
    @GetMapping("/info/region/{regionId}")
    private CommonResponse<PageResponse<List<MapRes.StoreInfo>>> getInfoByRegion(@AuthenticationPrincipal User user,
                                                                           @Parameter(description = "지역 이름 보내주는 region 값 보내주기", example = "0") @PathVariable("regionId") Long regionId,
                                                                           @Parameter(description = "페이지", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                           @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = true) Integer size) {
        PageResponse<List<MapRes.StoreInfo>> storeInfoList=mapService.getInfoByRegion(regionId,page,size);
        return CommonResponse.onSuccess(storeInfoList);
    }



    //쇼룸 마커
    @GetMapping("/{storeId}")
    @ApiOperation(value = "04-07 지도 쇼룸 검색 쇼룸 마커+하단 스와이프 조회 🗺 API FRAME SEARCH_03,04", notes = "검색 시 쇼룸 단일 조회  ")
    private CommonResponse<MapRes.StorePositionAndInfo> getStorePositionAndInfo(@Parameter(description = "storeId 값 보내주기", example = "0")  @PathVariable Long storeId){
        MapRes.StorePositionAndInfo storePositionAndInfo = mapService.getStorePositionAndInfo(storeId);
        return CommonResponse.onSuccess(storePositionAndInfo);
    }

}
