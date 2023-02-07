package com.example.runway.service;

import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.Store;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    @Override
    public List<MapRes.GetMapRes> getMainMap(Long userId) {
        List<Store> storeList=storeRepository.findAll();
        List<MapRes.GetMapRes> getMapList=new ArrayList<>();
        for (Store value : storeList){
            MapRes.GetMapRes map= StoreConvertor.StoreMapBuilder(value);
            getMapList.add(map);
        }
        return getMapList;
    }

    @Override
    public List<MapRes.GetMapRes> getMapFilter(Long userId, MapReq.FilterMap filterMap) {
        List<MapRes.GetMapRes> mapList=new ArrayList<>();
        List<String> categoryList=storeService.getCategoryList();

        List<StoreRepository.GetMapList> mapResult=null;

        if(filterMap.getCategory().size()==0){
            mapResult=storeRepository.getMapListFilter(categoryList);
        }
        else{
            mapResult=storeRepository.getMapListFilter(filterMap.getCategory());
        }

        mapResult.forEach(
                result->{
                    mapList.add(new MapRes.GetMapRes(
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
    public List<MapRes.GetStoreInfoListRes> getStoreInfoFilter(Long userId, MapReq.FilterMap filterMap) {
        List<MapRes.GetStoreInfoListRes> storeInfoList=new ArrayList<>();

        List<StoreRepository.StoreInfoList> storeResult= storeRepository.getStoreInfoFilter(filterMap.getCategory());

        storeResult.forEach(
                result->{
                    storeInfoList.add(new MapRes.GetStoreInfoListRes(
                            result.getStoreId(),
                            result.getStoreImg(),
                            Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                            result.getStoreName()
                    ));
                }
        );

        return storeInfoList;
    }

}
