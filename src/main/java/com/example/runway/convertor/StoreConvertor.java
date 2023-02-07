package com.example.runway.convertor;

import com.example.runway.domain.Store;
import com.example.runway.dto.map.MapRes;

public class StoreConvertor {
    public static MapRes.GetMapRes StoreMapBuilder(Store value) {
        return MapRes.GetMapRes.builder().storeId(value.getId()).storeName(value.getName()).latitude(value.getLatitude()).longitude(value.getLongitude()).build();
    }
}
