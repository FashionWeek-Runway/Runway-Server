package com.example.runway.repository;

import com.example.runway.domain.OwnerBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerBoardRepository extends JpaRepository<OwnerBoard,Long> {
    @Query(nativeQuery = true,value = "select img_url'imgUrl',store_id'storeId',title,DATE_FORMAT(OB.created_at,'%m.%d')'day' from OwnerBoard OB" +
            " left join OwnerBoardImg OBI on OB.id = OBI.board_id where store_id=:storeId order by OB.created_at asc",countQuery = "select * from OwnerBoard where store_id=:storeId")
    Page<OwnerBoardRepository.StoreBoardList> getStoreBoardList(@Param("storeId")Long storeId , Pageable pageReq);
    interface StoreBoardList {
        String getImgUrl();
        Long getStoreId();
        String getTitle();
        String getDay();
    }

    @Query(nativeQuery = true,value="select OB.user_id'userId',\n" +
            "       IF((select exists(select * from KeepOwnerBoard where KeepOwnerBoard.board_id=OB.id and KeepOwnerBoard.user_id = :userId)),'true','false')'bookmark',\n" +
            "        (select GROUP_CONCAT(OBI.img_url SEPARATOR ',') from OwnerBoardImg OBI where OBI.board_id = OB.id)'imgUrl',\n" +
            "       DATE_FORMAT(OB.created_at ,'%Y.%m.%d %h.%i')'day',\n" +
            "       OB.id'boardId',\n" +
            "       S.name'storeName',\n" +
            "       OB.title,\n" +
            "       OB.content,\n" +
            "       S.id'storeId',\n" +
            "       concat(R.region,', ',R.city)'regionInfo'\n" +
            "from OwnerBoard OB\n" +
            "join Store S on OB.store_id = S.id\n" +
            "join Region R on S.region_id = R.id\n" +
            "where OB.id = :boardId ")
    StoreBoard getStoreBoard(@Param("userId") Long userId,@Param("boardId") Long boardId);
    interface StoreBoard {
        Long getUserId();
        boolean getBookmark();
        String getImgUrl();
        String getDay();
        Long getBoardId();
        String getStoreName();
        String getTitle();
        String getContent();
        Long getStoreId();
        String getRegionInfo();
    }
}
