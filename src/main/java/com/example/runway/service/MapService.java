package com.example.runway.service;

import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;

import java.util.List;

public interface MapService {
    List<MapRes.GetMapRes> getMainMap(Long userId);

    List<MapRes.GetMapRes> getMapFilter(Long userId, MapReq.FilterMap filterMap);

    List<MapRes.GetStoreInfoListRes> getStoreInfoFilter(Long userId, MapReq.FilterMap filterMap);
}
