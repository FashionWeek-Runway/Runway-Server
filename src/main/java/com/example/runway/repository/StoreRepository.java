package com.example.runway.repository;

import com.example.runway.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {


    @Query(value="select S.id 'storeId',S.name'storeName',latitude,longitude" +
            " from Store S join StoreCategory SC on S.id = SC.store_id" +
            " join Category C on SC.category_id = C.id where C.category IN (:categoryList) group by S.id",nativeQuery = true)
    List<GetMapList> getMapListFilter(@Param("categoryList") List<String> categoryList);
    interface GetMapList{
        Long getStoreId();
        String getStoreName();
        double getLatitude();
        double getLongitude();
    }

    @Query(value="select S.id 'storeId',\n" +
            "       S.name'storeName',\n" +
            "       SI.store_img'storeImg'\n" +
            "       ,\n" +
            "       (select GROUP_CONCAT(C.category SEPARATOR ',')\n" +
            "        from Category C\n" +
            "                 join StoreCategory SC on SC.category_id = C.id\n" +
            "                 join Store S2 on SC.store_id = S2.id\n" +
            "        where S.id = S2.id\n" +
            "       )as 'storeCategory'\n" +
            "from Store S join StoreCategory SC on S.id = SC.store_id\n" +
            "join Category C on SC.category_id = C.id\n" +
            "join StoreImg SI on S.id = SI.store_id where C.category IN (:categoryList) and sequence=1  group by S.id;",nativeQuery = true)
    List<StoreInfoList> getStoreInfoFilter(@Param("categoryList") List<String> categoryList);
    interface StoreInfoList {
        Long getStoreId();
        String getStoreImg();
        String getStoreCategory();
        String getStoreName();
    }
}
