package com.example.runway.service;

import com.example.runway.dto.PageResponse;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;

import java.util.List;

public interface MapService {
    //List<MapRes.Map> getMainMap(Long userId);

    List<MapRes.Map> getMapFilter(Long userId, MapReq.FilterMap filterMap);

    PageResponse<List<MapRes.StoreInfo>> getStoreInfoFilter(Long userId, MapReq.FilterMap filterMap, Integer page, Integer size);

    MapRes.SearchList getStoreBySearch(MapReq.SearchStore searchStore);

    MapRes.StoreInfo getStoreByStoreId(Long storeId);

    List<MapRes.MapMarkerList> getStoreByRegion(Long region);

    MapRes.StorePositionAndInfo getStorePositionAndInfo(Long storeId);

    PageResponse<List<MapRes.StoreInfo>> getInfoByRegion(Long regionId, Integer page, Integer size);
}
