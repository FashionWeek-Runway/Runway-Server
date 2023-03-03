package com.example.runway.service.map;

import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.Region;
import com.example.runway.domain.Store;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.map.MapReq;
import com.example.runway.dto.map.MapRes;
import com.example.runway.repository.RegionRepository;
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
    private final RegionRepository regionRepository;


    @Override
    public List<MapRes.Map> getMapFilter(Long userId, MapReq.FilterMap filterMap) {
        List<MapRes.Map> mapList=new ArrayList<>();

        List<StoreRepository.GetMapList> mapResult=null;

        if(filterMap.getCategory().size()==0){
            mapResult=storeRepository.getMapListFilter(CATEGORY,userId,filterMap.getLatitude(),filterMap.getLongitude());
        }
        else{
            mapResult=storeRepository.getMapListFilter(filterMap.getCategory(),userId, filterMap.getLatitude(), filterMap.getLongitude());
        }

        mapResult.forEach(
                result->{
                    mapList.add(new MapRes.Map(
                            result.getStoreId(),
                            result.getStoreName(),
                            result.getBookMark(),
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
        Page<StoreRepository.StoreInfoList> storeResult=null;

        if(filterMap.getCategory().size()==0){
            storeResult = storeRepository.getStoreInfoFilter(CATEGORY,pageReq, filterMap.getLatitude(), filterMap.getLongitude(),userId);
        }
        else{
            storeResult= storeRepository.getStoreInfoFilter(filterMap.getCategory(),pageReq,filterMap.getLatitude(),filterMap.getLongitude(),userId);
        }


        storeResult.forEach(
                result-> storeInfoList.add(new MapRes.StoreInfo(
                        result.getStoreId(),
                        result.getStoreImg(),
                        Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                        result.getStoreName(),
                        result.getDistance(),
                        result.getBookmark()
                ))
        );



        return new PageResponse<>(storeResult.isLast(),storeInfoList);
    }


    @Override
    public MapRes.SearchList getStoreBySearch(MapReq.SearchStore searchStore) {

        List<Region> region=regionRepository.findByRegionContaining(searchStore.getContent());
        List<MapRes.RegionSearchList> searchList=new ArrayList<>();

        region.forEach(
                result->{
                    searchList.add(
                            new MapRes.RegionSearchList(
                                    result.getId(),
                                    result.getRegion(),
                                    result.getAddress()
                            )
                    );
                }
        );


        List<StoreRepository.StoreInfoList> storeSearchListResult=storeRepository.getStoreSearch(searchStore.getLatitude(),searchStore.getLongitude(),searchStore.getContent());
        List<MapRes.StoreSearchList> storeSearchList=new ArrayList<>();

        storeSearchListResult.forEach(
                result->{
                    storeSearchList.add(
                            new MapRes.StoreSearchList(
                                    result.getStoreId(),
                                    result.getStoreName(),
                                    result.getAddress(),
                                    result.getDistance()
                            )
                    );
                }
        );


        //List<MapRes.StoreSearchList> storeSearchList=jdbcQuery.getStoreBySearch(searchStore);


        return new MapRes.SearchList(searchList,storeSearchList);
    }

    @Override
    public MapRes.StoreInfo getStoreByStoreId(Long storeId) {
        StoreRepository.StoreInfoList storeResult=storeRepository.getSingleStore(storeId);

        return StoreConvertor.StoreInfo(storeResult,Stream.of(storeResult.getStoreCategory().split(",")).collect(Collectors.toList()));
    }

    @Override
    public List<MapRes.MapMarkerList> getStoreByRegion(Long regionId) {

        List<Store> store=storeRepository.findByRegionId(regionId);
        List<MapRes.MapMarkerList> markerList=new ArrayList<>();

        store.forEach(
                result-> {
                    markerList.add(new MapRes.MapMarkerList(
                            result.getId(),
                            result.getName(),
                            result.getAddress(),
                            result.getLatitude(),
                            result.getLongitude()

                    ));
                }
        );

        return markerList;
    }

    @Override
    public MapRes.StorePositionAndInfo getStorePositionAndInfo(Long storeId) {

        StoreRepository.StoreInfoList storeResult=storeRepository.getSingleStore(storeId);

        MapRes.MapMarkerList marker= StoreConvertor.PositionBuilder(storeResult);

        MapRes.StoreInfo storeInfo=StoreConvertor.StoreInfo(storeResult,Stream.of(storeResult.getStoreCategory().split(",")).collect(Collectors.toList()));

        return new MapRes.StorePositionAndInfo(marker,storeInfo);
    }

    @Override
    public PageResponse<List<MapRes.StoreInfo>> getInfoByRegion(Long regionId, Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);
        List<MapRes.StoreInfo> storeInfoList=new ArrayList<>();
        Page<StoreRepository.StoreInfoList> storeInfoResult=storeRepository.getStoreInfoRegion(regionId,pageReq);
        storeInfoResult.forEach(
                result-> {
                        storeInfoList.add(new MapRes.StoreInfo(
                                result.getStoreId(),
                                result.getStoreImg(),
                                Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                                result.getStoreName(),
                                result.getDistance(),
                                result.getBookmark()
                        ));
                }

        );
        return new PageResponse<>(storeInfoResult.isLast(),storeInfoList);
    }



}
