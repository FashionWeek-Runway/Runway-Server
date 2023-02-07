package com.example.runway.controller;

import com.example.runway.common.CommonResponse;
import com.example.runway.domain.User;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.service.StoreService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "03 - 쇼룸🏬")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;
    @GetMapping("")
    private CommonResponse<StoreRes.getHomeList> getMainHome(@AuthenticationPrincipal User user){
        Long userId=user.getId();
        StoreRes.getHomeList getHomeList=storeService.getMainHome(userId);
        return CommonResponse.onSuccess(getHomeList);
    }
}
