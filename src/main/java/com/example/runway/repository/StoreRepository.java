package com.example.runway.repository;

import com.example.runway.domain.Store;
import com.example.runway.dto.map.MapRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {


    @Query(value="select S.id 'storeId',S.name'storeName',latitude,longitude" +
            " from Store S join StoreCategory SC on S.id = SC.store_id" +
            " join Category C on SC.category_id = C.id where C.category IN (:categoryList) group by S.id",nativeQuery = true)
    List<GetMapList> getMapListFilter(@Param("categoryList") List<String> categoryList);

    boolean existsByIdAndStatus(Long storeId, boolean b);



    List<Store> findByNameContainingOrAddressContainingOrRegionContaining(String content, String content1, String content2);


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
            "join StoreImg SI on S.id = SI.store_id where C.category IN (:categoryList) and sequence=1 and S.status=true group by S.id ",
            countQuery = "select count(*) from Store S join StoreCategory SC on S.id = SC.store_id \n" +
                    "join Category C on SC.category_id = C.id \n" +
                    "join StoreImg SI on S.id = SI.store_id where C.category IN (:categoryList) and sequence=1 and S.status=true group by S.id",
            nativeQuery = true)
    Page<StoreInfoList> getStoreInfoFilter(@Param("categoryList") List<String> categoryList, Pageable pageReq);
    interface StoreInfoList {
        Long getStoreId();
        String getStoreImg();
        String getStoreCategory();
        String getStoreName();
    }

    @Query(value = "select S.id             'storeId',\n" +
            "       S.name           'storeName',\n" +
            "       S.address,\n" +
            "       S.time           'storeTime',\n" +
            "       S.phone_number   'storePhone',\n" +
            "       S.instagram_link 'instagram',\n" +
            "       S.website,\n" +
            "       (select GROUP_CONCAT(C.category SEPARATOR ',')\n" +
            "                    from Category C\n" +
            "                             join StoreCategory SC on SC.category_id = C.id\n" +
            "                             join Store S2 on SC.store_id = S2.id\n" +
            "                    where S.id = S2.id\n" +
            "                   )as 'category'\n" +
            "from Store S where S.id=:storeId and S.status=true;",nativeQuery = true)
    StoreInfo getStoreInfo(@Param("storeId") Long storeId);
    interface StoreInfo {
        Long getStoreId();
        String getCategory();
        String getStoreName();
        String getAddress();
        String getWebsite();
        String getStoreTime();
        String getStorePhone();
        String getInstagram();
    }
}
