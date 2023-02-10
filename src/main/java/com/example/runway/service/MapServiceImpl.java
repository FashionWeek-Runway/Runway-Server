package com.example.runway.service;

import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.Store;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.runway.constants.Constants.CATEGORY;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    @Override
    public List<MapRes.Map> getMainMap(Long userId) {
        List<Store> storeList=storeRepository.findAll();
        List<MapRes.Map> getMapList=new ArrayList<>();
        for (Store value : storeList){
            MapRes.Map map= StoreConvertor.StoreMapBuilder(value);
            getMapList.add(map);
        }
        return getMapList;
    }

    @Override
    public List<MapRes.Map> getMapFilter(Long userId, MapReq.FilterMap filterMap) {
        List<MapRes.Map> mapList=new ArrayList<>();

        List<StoreRepository.GetMapList> mapResult=null;

        if(filterMap.getCategory().size()==0){
            mapResult=storeRepository.getMapListFilter(CATEGORY);
        }
        else{
            mapResult=storeRepository.getMapListFilter(filterMap.getCategory());
        }

        mapResult.forEach(
                result->{
                    mapList.add(new MapRes.Map(
                            result.getStoreId(),
                            result.getStoreName(),
                            result.getLatitude(),
                            result.getLongitude()
                    ));
                }
        );

        return mapList;


    }

    @Override
    public PageResponse<List<MapRes.StoreInfo>> getStoreInfoFilter(Long userId, MapReq.FilterMap filterMap, Integer page, Integer size) {
        List<MapRes.StoreInfo> storeInfoList=new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);
        Page<StoreRepository.StoreInfoList> storeResult= storeRepository.getStoreInfoFilter(filterMap.getCategory(),pageReq);

        storeResult.forEach(
                result->{
                    storeInfoList.add(new MapRes.StoreInfo(
                            result.getStoreId(),
                            result.getStoreImg(),
                            Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                            result.getStoreName()
                    ));
                }
        );

        return new PageResponse<>(storeResult.isLast(),storeInfoList);
    }

    @Override
    public List<MapRes.StoreSearchList> getStoreBySearch(String content) {
        return null;
    }

}
