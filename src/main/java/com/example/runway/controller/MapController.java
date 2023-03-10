package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.service.map.MapService;
import com.example.runway.service.store.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "04-๋งต ๐บ")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/maps")
public class MapController {
    private final MapService mapService;
    private final StoreService storeService;


    //TODO ๊ฑฐ๋ฆฌ์์ผ๋ก ์กฐํ์์ผ์ฃผ๊ธฐ
    @ApiOperation(value = "04-01 ๋ฉ์ธ ์ง๋ ์กฐํ + ํํฐ๋ง ์กฐํ ๋ง์ปค์ฉ ๐บ API FRAME MAP_03,04 ", notes = "04-02 ์ ํจ๊ป ์กฐํํด์ผํฉ๋๋ค ์ง๋ ์กฐํ API ArrayList ์ ์๋ฌด๊ฒ๋ ์๋ ฅ ์ํ  ์ ์ ์ฒด ์กฐํ. example = category=[]")
    @PostMapping("/filter")
    private CommonResponse<List<MapRes.Map>> getMapFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap){
        log.info("get-main-map");
        log.info("api = get-main-map 04-01");
        Long userId=user.getId();
        List<MapRes.Map> mapList=mapService.getMapFilter(userId,filterMap);
        return CommonResponse.onSuccess(mapList);
    }

    @ApiOperation(value = "04-02 ํ๋จ ์ค์์ดํ ์ผ๋ฃธ ํํฐ๋ง ์กฐํ ๐บ API FRAME MAP_03,04", notes = "ํ๋จ ๋ฐ ์ผ๋ฃธ ํํฐ๋ง ์กฐํ API ArrayList์ ์๋ฌด ๊ฒ๋ ์๋ ฅ ์ํ  ์ ์ ์ฒด ์กฐํ")
    @PostMapping("/info")
    private CommonResponse<PageResponse<List<MapRes.StoreInfo>>> getStoreInfoFilter(@AuthenticationPrincipal User user, @RequestBody MapReq.FilterMap filterMap,
                                                                                              @Parameter(description = "ํ์ด์ง", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                                              @Parameter(description = "ํ์ด์ง ์ฌ์ด์ฆ", example = "10") @RequestParam(required = true) Integer size){
        log.info("get-main-swipe");
        log.info("api = get-main-swipe 04-02");
        Long userId=user.getId();
        PageResponse<List<MapRes.StoreInfo>> storeInfoList=mapService.getStoreInfoFilter(userId,filterMap,page,size);
        return CommonResponse.onSuccess(storeInfoList);
    }

    //๊ฒ์
    @ApiOperation(value = "04-03 ์ง๋ ์ผ๋ฃธ ๊ฒ์ ์ง๋ ์กฐํ ๊ฒ์์ฉ ๐บ API FRAME SEARCH_07", notes = "์ง๋ ๊ฒ์ ์กฐํ")
    @PostMapping("/search")
    private CommonResponse<MapRes.SearchList> getContentsBySearch(@AuthenticationPrincipal User user, @RequestBody MapReq.SearchStore searchStore) {
        log.info("get-search");
        log.info("api = get-search-by-content 04-03");
        MapRes.SearchList storeSearchList=mapService.getStoreBySearch(searchStore);
        return CommonResponse.onSuccess(storeSearchList);
    }

    @ApiOperation(value = "04-04 ์ง๋ ๋งค์ฅ ๋จ์ผ ์ ํ ํ๋จ ์ค์์ดํ ์กฐํ ๐บ API FRAME MAP_07", notes = "์ง๋ ํํฐ๋ง ์กฐํ")
    @GetMapping("/info/{storeId}")
    private CommonResponse<MapRes.StoreInfo> getStoreByStoreId(@AuthenticationPrincipal User user,@Parameter(description = "storeId ๊ฐ ๋ณด๋ด์ฃผ๊ธฐ", example = "0")  @PathVariable Long storeId) {
        log.info("get-single-store");
        log.info("api = get-single-store 04-04");
        MapRes.StoreInfo storeInfo = mapService.getStoreByStoreId(storeId);
        return CommonResponse.onSuccess(storeInfo);
    }

    //์ง์ญ ๋ง์ปค
    @ApiOperation(value = "04-05 ์ง๋ ์ผ๋ฃธ ๊ฒ์ ์ง์ญ ๋ง์ปค์ฉ ๐บ API FRAME SEARCH_01", notes = "์ง์ญ์ ์๋ ๋ชจ๋  ์ผ๋ฃธ ์ ๋ณด")
    @GetMapping("/region/{regionId}")
    private CommonResponse<List<MapRes.MapMarkerList>> getStoreByRegion(@AuthenticationPrincipal User user, @Parameter(description = "์ง์ญ ์ด๋ฆ ๋ณด๋ด์ฃผ๋ region ๊ฐ ๋ณด๋ด์ฃผ๊ธฐ", example = "0") @PathVariable("regionId") Long regionId) {
        log.info("get-region-marker");
        log.info("api = get-region-marker 04-05");
        List<MapRes.MapMarkerList> storeSearchList=mapService.getStoreByRegion(regionId);
        return CommonResponse.onSuccess(storeSearchList);
    }


    @ApiOperation(value = "04-06 ์ง๋ ์ผ๋ฃธ ๊ฒ์ ์ง์ญ ์ค์์ดํ์ฉ ๐บ API FRAME SEARCH_02", notes = "์ง๋ ๊ฒ์ ์กฐํ")
    @GetMapping("/info/region/{regionId}")
    private CommonResponse<PageResponse<List<MapRes.StoreInfo>>> getInfoByRegion(@AuthenticationPrincipal User user,
                                                                           @Parameter(description = "์ง์ญ ์ด๋ฆ ๋ณด๋ด์ฃผ๋ region ๊ฐ ๋ณด๋ด์ฃผ๊ธฐ", example = "0") @PathVariable("regionId") Long regionId,
                                                                           @Parameter(description = "ํ์ด์ง", example = "0") @RequestParam(required = true) @Min(value = 0) Integer page,
                                                                           @Parameter(description = "ํ์ด์ง ์ฌ์ด์ฆ", example = "10") @RequestParam(required = true) Integer size) {
        log.info("get-region-swipe");
        log.info("api = get-region-swipe 04-06");
        Long userId=user.getId();
        PageResponse<List<MapRes.StoreInfo>> storeInfoList=mapService.getInfoByRegion(regionId,page,size,userId);
        return CommonResponse.onSuccess(storeInfoList);
    }



    //์ผ๋ฃธ ๋ง์ปค
    @GetMapping("/{storeId}")
    @ApiOperation(value = "04-07 ์ง๋ ์ผ๋ฃธ ๊ฒ์ ์ผ๋ฃธ ๋ง์ปค+ํ๋จ ์ค์์ดํ ์กฐํ ๐บ API FRAME SEARCH_03,04", notes = "๊ฒ์ ์ ์ผ๋ฃธ ๋จ์ผ ์กฐํ  ")
    private CommonResponse<MapRes.StorePositionAndInfo> getStorePositionAndInfo(@Parameter(description = "storeId ๊ฐ ๋ณด๋ด์ฃผ๊ธฐ", example = "0")  @PathVariable Long storeId){
        log.info("get-single-store");
        log.info("api = get-single-store 04-07");
        MapRes.StorePositionAndInfo storePositionAndInfo = mapService.getStorePositionAndInfo(storeId);
        return CommonResponse.onSuccess(storePositionAndInfo);
    }

}
