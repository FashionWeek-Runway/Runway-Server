package com.example.runway.repository;

import com.example.runway.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {


    @Query(value="select S.id 'storeId',S.name'storeName',IF((select exists(select * from Keep where Keep.store_id=S.id and Keep.user_id=:userId)),'true','false')'bookmark'," +
            " latitude,longitude, C2.category'storeCategory', " +
            "   (6371*acos(cos(radians(:latitude))*cos(radians(S.latitude))*cos(radians(S.longitude)" +
            "   -radians(:longitude))+sin(radians(:latitude))*sin(radians(S.latitude))))as distance " +
            " from Store S join StoreCategory SC on S.id = SC.store_id" +
            " join Category C on SC.category_id = C.id " +
            " join Category C2 on C2.id=main_category where C.category IN (:categoryList) group by S.id order by distance",nativeQuery = true)
    List<GetMapList> getMapListFilter(@Param("categoryList") List<String> categoryList, @Param("userId") Long userId, @Param("latitude") double latitude,@Param("longitude") double longitude);


    interface GetMapList{
        Long getStoreId();
        String getStoreName();
        String getStoreCategory();
        boolean getBookMark();
        double getLatitude();
        double getLongitude();
    }

    boolean existsByIdAndStatus(Long storeId, boolean b);







    @Query(value="select S.id 'storeId',\n" +
            "       S.name'storeName',\n" +
            "       SI.store_img'storeImg',C.category 'mainCategory'\n" +
            "       ,\n" +
            "       (select GROUP_CONCAT(C2.category SEPARATOR ',')\n" +
            "        from Category C2\n" +
            "                 join StoreCategory SC2 on SC2.category_id = C2.id\n" +
            "                 join Store S2 on SC2.store_id = S2.id \n" +
            "        where S2.id = S.id \n" +
            "       )as 'storeCategory',\n" +
            "   (6371*acos(cos(radians(:latitude))*cos(radians(S.latitude))*cos(radians(S.longitude)" +
            "   -radians(:longitude))+sin(radians(:latitude))*sin(radians(S.latitude))))as distance " +
            "from Store S join StoreCategory SC on S.id = SC.store_id\n" +
            "join Category C on SC.category_id = C.id "  +
            "join StoreImg SI on S.id = SI.store_id where C.category IN (:categoryList) and sequence=1 and S.status=true group by S.id order by distance ",
            countQuery = "select count(*) from Store S join StoreCategory SC on S.id = SC.store_id \n" +
                    "join Category C on SC.category_id = C.id \n" +
                    "join StoreImg SI on S.id = SI.store_id where C.category IN (:categoryList) and sequence=1 and S.status=true group by S.id",
            nativeQuery = true)
    Page<StoreInfoList> getStoreInfoFilter(@Param("categoryList") List<String> categoryList, Pageable pageReq,@Param("latitude") double latitude,@Param("longitude") double longitude);
    interface StoreInfoList {
        Long getStoreId();
        String getStoreImg();
        String getMainCategory();
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



    @Query(nativeQuery = true,value = "select S.id, S.name, S.address," +
            "   (6371*acos(cos(radians(:latitude))*cos(radians(S.latitude))*cos(radians(S.longitude)" +
            "   -radians(:longitude))+sin(radians(:latitude))*sin(radians(S.latitude))))as distance " +
            "    from Store S join Region R on S.region_id = R.id " +
            "    where S.name LIKE concat('%',:content,'%') or S.address LIKE concat('%',:content,'%') or R.region LIKE concat('%',:content,'%') " +
            "    order by distance")
    List<StoreSearch> getStoreSearch(@Param("content") String content,@Param("latitude") Double latitude,@Param("longitude") Double longitude);
    interface StoreSearch {
        Long getId();
        String getName();
        String getAddress();
    }
}
