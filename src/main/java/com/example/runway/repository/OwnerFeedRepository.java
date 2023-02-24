package com.example.runway.repository;

import com.example.runway.domain.OwnerFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerFeedRepository extends JpaRepository<OwnerFeed,Long> {
    @Query(nativeQuery = true,value = "select img_url'imgUrl',store_id'storeId',title,DATE_FORMAT(OB.created_at,'%m.%d')'day' from OwnerFeed OB" +
            " left join OwnerFeedImg OBI on OB.id = OBI.feed_id where store_id=:storeId order by OB.created_at asc",countQuery = "select count(*) from OwnerFeed where store_id=:storeId")
    Page<OwnerFeedRepository.StoreBoardList> getStoreBoardList(@Param("storeId")Long storeId , Pageable pageReq);
    interface StoreBoardList {
        String getImgUrl();
        Long getStoreId();
        String getTitle();
        String getDay();
    }

    @Query(nativeQuery = true,value="select OB.user_id'userId',\n" +
            "       IF((select exists(select * from KeepOwnerFeed where KeepOwnerFeed.feed_id=OB.id and KeepOwnerFeed.user_id = :userId)),'true','false')'bookmark',\n" +
            "        (select GROUP_CONCAT(OBI.img_url SEPARATOR ',') from OwnerFeedImg OBI where OBI.feed_id = OB.id)'imgUrl',\n" +
            "       DATE_FORMAT(OB.created_at ,'%Y.%m.%d %h.%i')'day',\n" +
            "       OB.id'feedId',\n" +
            "       S.name'storeName',\n" +
            "       OB.title,\n" +
            "       OB.content,\n" +
            "       S.id'storeId',\n" +
            "       concat(R.region,', ',R.city)'regionInfo'\n" +
            "from OwnerFeed OB\n" +
            "join Store S on OB.store_id = S.id\n" +
            "join Region R on S.region_id = R.id\n" +
            "where OB.id = :boardId ")
    StoreBoard getStoreBoard(@Param("userId") Long userId,@Param("boardId") Long boardId);
    interface StoreBoard {
        Long getUserId();
        boolean getBookmark();
        String getImgUrl();
        String getDay();
        Long getFeedId();
        String getStoreName();
        String getTitle();
        String getContent();
        Long getStoreId();
        String getRegionInfo();
    }
}
