package com.example.runway.repository;

import com.example.runway.domain.OwnerBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerBoardRepository extends JpaRepository<OwnerBoard,Long> {
    @Query(nativeQuery = true,value = "select img_url'imgUrl',store_id'storeId',title,concat(MONTH(OB.created_at),':',DAY(OB.created_at))'day' from OwnerBoard OB" +
            " left join OwnerBoardImg OBI on OB.id = OBI.board_id where store_id=:storeId order by OB.created_at asc",countQuery = "select * from OwnerBoard where store_id=:storeId")
    Page<OwnerBoardRepository.StoreBoardList> getStoreBoardList(@Param("storeId")Long storeId , Pageable pageReq);
    interface StoreBoardList {
        String getImgUrl();
        Long getStoreId();
        String getTitle();
        String getDay();
    }
}
