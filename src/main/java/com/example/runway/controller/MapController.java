package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.service.MapService;
import com.example.runway.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "04-맵 🗺")
@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {
    private final MapService mapService;


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


}
