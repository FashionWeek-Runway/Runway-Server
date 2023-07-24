package com.example.runway.repository;

import com.example.runway.domain.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StoreReviewRepository extends JpaRepository<StoreReview,Long> {
    Page<StoreReview> findByStoreIdAndDeletedOrderByCreatedAtDescIdAsc(Long storeId,  boolean b,Pageable pageReq);

    @Query(nativeQuery = true,value="select SR.id   'reviewId',SR.user_id'userId',\n" +
            "       U.profile_url 'profileImgUrl',\n" +
            "       U.nickname,\n" +
            "       SR.img_url    'imgUrl',\n" +
            "       SR.store_id   'storeId',\n" +
            "       S.name        'storeName',\n" +
            "       concat(R.region,', ',R.city)'regionInfo'," +
            "       SR.created_at'createdAt'," +
            "       IF((select exists(select * from ReviewKeep RK where RK.review_id=:reviewId and RK.user_id=:userId)),'true','false')'bookmark'," +
            "       count(K.review_id)'bookmarkCnt'\n" +
            "from StoreReview SR\n" +
            "         join User U on U.id = SR.user_id\n" +
            "         join Store S on S.id = SR.store_id\n" +
            "         join Region R on R.id = S.region_id\n" +
            "         join ReviewKeep K on K.review_id=:reviewId " +
            "where SR.id=:reviewId and SR.deleted=true")
    StoreReviewRepository.GetStoreReview getStoreReview(@Param("reviewId") Long reviewId, @Param("userId") Long userId);

    @Query(nativeQuery = true,value="select SR.id'reviewId',SR.user_id'userId',\n" +
            "       U.profile_url 'profileImgUrl',\n" +
            "       U.nickname,\n" +
            "       SR.img_url    'imgUrl',\n" +
            "       SR.store_id   'storeId',\n" +
            "       S.name        'storeName',SR.created_at'createdAt',\n" +
            "       concat(R.region,', ',R.city)'regionInfo',\n" +
            "       SUM(CASE WHEN C.category IN (:categoryList) THEN 1 ELSE 0 END) AS categoryScore,\n" +
            "       IF((select exists(select * from ReviewKeep RK where RK.review_id=:reviewId and RK.user_id=:userId)),'true','false')'bookmark',\n" +
            "       (select count(*) from ReviewKeep RK where RK.review_id=:reviewId)'bookmarkCnt'\n" +
            "from StoreReview SR\n" +
            "         join User U on U.id = SR.user_id\n" +
            "         join Store S on S.id = SR.store_id\n" +
            "         join Region R on R.id = S.region_id\n" +
            "         join StoreCategory SC on S.id = SC.store_id\n" +
            "         join Category C on C.id = SC.category_id\n" +
            "where SR.id=:reviewId and SR.deleted=true  ")
    StoreReviewRepository.GetStoreReview getStoreReviewRecommend(@Param("reviewId") Long reviewId, @Param("userId") Long userId,@Param("categoryList") List<String> categoryList);

    boolean existsByIdAndStatus(Long reviewId, boolean b);

    boolean existsByIdAndDeleted(Long reviewId, boolean b);

    @Query(value = "select SR.id,SR.img_url'imgUrl' from StoreReview SR " +
            " where SR.user_id=:userId and SR.created_at>:createdAt and SR.id != :reviewId and SR.deleted =true " +
            "  order by SR.created_at asc limit 1",nativeQuery = true)
    GetReviewId findPrevMuReviewId(@Param("createdAt") LocalDateTime createdAt,@Param("userId") Long userId,@Param("reviewId") Long reviewId);

    @Query(value = "select SR.id,SR.img_url'imgUrl' from StoreReview SR" +
            " where SR.user_id=:userId and SR.created_at<:createdAt and SR.id != :reviewId and SR.deleted =true order by created_at desc limit 1",nativeQuery = true)
    GetReviewId findNextMyReviewId(@Param("createdAt") LocalDateTime createdAt,@Param("userId") Long userId,@Param("reviewId") Long reviewId);

    @Query(value = "select SR.id         'reviewId',SR.user_id'userId',\n" +
            "       U.profile_url 'profileImgUrl',\n" +
            "       U.nickname,\n" +
            "       SR.img_url    'imgUrl',S.id'storeId',\n" +
            "       S.name        'storeName',\n" +
            "       concat(R.region, ', ', R.city)'regionInfo',\n" +
            "       count(RK.review_id)'bookmarkCnt',SR.created_at'createdAt'," +
            "       IF((select exists(select * from ReviewKeep RK where RK.review_id=:reviewId and RK.user_id=:userId)),'true','false')'bookmark'\n" +
            "from StoreReview SR\n" +
            "         join User U on SR.user_id = U.id\n" +
            "         join Store S on SR.store_id = S.id\n" +
            "         join Region R on S.region_id = R.id\n" +
            "         left join ReviewKeep RK on SR.id = RK.review_id\n" +
            "where SR.id=:reviewId and RK.review_id=SR.id and RK.user_id=:userId and SR.deleted =true ",nativeQuery = true)
    GetStoreReview getMyBookmarkReview(@Param("reviewId") Long reviewId,@Param("userId") Long userId);


    @Query(value = "select SR.id,SR.img_url'imgUrl'\n" +
            "from StoreReview SR\n" +
            "join ReviewKeep RK on SR.id = RK.review_id " +
            "where RK.user_id=:userId and SR.created_at < :createdAt\n" +
            "  and SR.id != :reviewId and SR.deleted =true \n" +
            "   or (created_at = :createdAt AND id > :reviewId)\n" + "order by created_at desc, SR.id desc limit 1",nativeQuery = true)
    GetReviewId findNextBookMarkReviewId(@Param("createdAt") LocalDateTime createdAt,@Param("userId") Long userId,@Param("reviewId") Long reviewId);

    @Query(value = "select SR.id,SR.img_url'imgUrl' \n" +
            "from StoreReview SR\n" +
            "join ReviewKeep RK on SR.id = RK.review_id " +
            "where RK.user_id=:userId and SR.created_at > :createdAt\n" +
            "    and SR.id != :reviewId and SR.deleted =true \n" +
            "   or (created_at = :createdAt AND id<:reviewId)\n" +
            "order by SR.created_at asc, SR.id desc limit 1 ",nativeQuery = true)
    GetReviewId findPrevBookMarkReviewId(@Param("createdAt") LocalDateTime createdAt,@Param("userId") Long userId,@Param("reviewId") Long reviewId);

    Optional<Object> findByIdAndStatus(Long reviewId, boolean b);

    List<StoreReview> findByUserId(Long id);


    interface GetCountAllReview {
        int getSize();
    }
    @Query(value = "select SR.id,SR.img_url'imgUrl' \n" +
                "from StoreReview SR  join Store S on SR.store_id = S.id " +
            "join StoreReview SR2 on SR2.store_id=S.id and SR2.id=:reviewId \n" +
                "where SR.store_id = :storeId\n" +
                "  and SR.created_at > :createdAt\n" +
                "    and SR.id != :reviewId and SR.deleted =true \n" +
                "   or (SR.created_at = :createdAt AND SR.id<:reviewId)\n" +
                "order by SR.created_at asc, SR.id desc limit 1",nativeQuery = true)
    StoreReviewRepository.GetReviewId findPrevReviewId(@Param("createdAt") LocalDateTime createdAt,@Param("storeId") Long storeId,@Param("reviewId") Long reviewId);


    @Query(value = "select SR.id,SR.img_url'imgUrl'\n" +
            "from StoreReview SR  " +
            "join Store S on SR.store_id = S.id join StoreReview SR2 on SR2.store_id=S.id and SR2.id=:reviewId\n" +
            "where SR.store_id = :storeId\n" +
            "  and SR.created_at < :createdAt\n" +
            "  and SR.id != :reviewId and SR.deleted =true \n " +
            "   or (SR.created_at = :createdAt AND SR.id > :reviewId)\n" +
            "order by SR.created_at desc, SR.id asc limit 1",nativeQuery = true)
    StoreReviewRepository.GetReviewId findNextReviewId(@Param("createdAt") LocalDateTime createdAt, @Param("storeId") Long storeId, @Param("reviewId") Long reviewId);


    @Query(value = "select SR.id         'reviewId',SR.user_id'userId',\n" +
            "       U.profile_url 'profileImgUrl',\n" +
            "       U.nickname,\n" +
            "       SR.img_url    'imgUrl',S.id'storeId',\n" +
            "       S.name        'storeName',\n" +
            "       concat(R.region, ', ', R.city)'regionInfo',\n" +
            "       count(RK.review_id)'bookmarkCnt',SR.created_at'createdAt'\n" +
            "from StoreReview SR\n" +
            "         join User U on SR.user_id = U.id\n" +
            "         join Store S on SR.store_id = S.id\n" +
            "         join Region R on S.region_id = R.id\n" +
            "         left join ReviewKeep RK on SR.id = RK.review_id\n" +
            "where SR.id=:reviewId and SR.deleted =true ",nativeQuery = true)
    GetStoreReview getMyReview(@Param("reviewId") Long reviewId);
    interface GetStoreReview {
        Long getReviewId();
        Long getUserId();
        String getProfileImgUrl();
        String getNickname();
        String getImgUrl();
        Long getStoreId();
        String getStoreName();
        String getRegionInfo();
        LocalDateTime getCreatedAt();
        boolean getBookMark();
        int getBookmarkCnt();
        int getCategoryScore();
    }


    @Query(value = "select SR.id 'reviewId', SR.img_url'imgUrl',concat(R.region,'/',R.city)'regionInfo'\n" +
            "from StoreReview SR\n" +
            "join Store S on SR.store_id = S.id\n" +
            "join Region R on S.region_id = R.id\n" +
            "where SR.user_id = :userId and SR.deleted = true order by SR.created_at desc",
            countQuery = "select count(*) from StoreReview SR where SR.user_id=:userId and SR.deleted=true",nativeQuery = true)
    Page<StoreReviewRepository.GetReviewInfo> GetReviewInfo(@Param("userId") Long userId, Pageable pageReq);
    interface GetReviewInfo {
        Long getReviewId();
        String getImgUrl();
        String getRegionInfo();
    }


    interface GetReviewId{
        Long getId();
        String getImgUrl();
    }

    @Query(value = "select SR.id,\n" +
            "       (select count(*) from ReviewKeep RK where RK.review_id = SR.id)                                                  'bookmarkCnt',\n" +
            "       S.name,\n" +
            "       SUM(CASE WHEN C.category IN (:categoryList) THEN 1 ELSE 0 END) AS categoryScore,\n" +
            "       SR.created_at,SR.img_url'imgUrl'\n" +
            "from StoreReview SR\n" +
            "         join Store S on S.id = SR.store_id\n" +
            "         join StoreCategory SC on S.id = SC.store_id\n" +
            "         join Category C on C.id = SC.category_id\n" +
            "where C.category IN (:categoryList) and SR.deleted = true and SR.id !=:reviewId or (SR.created_at = :createdAt AND SR.id > :reviewId)\n" +
            "group by SR.id\n" +
            "HAVING categoryScore <= :categoryScore\n" +
            "  AND IF(categoryScore = :categoryScore, SR.id < :reviewId, 1) = 1 " +
            "order by  categoryScore DESC,SR.created_at desc,SR.id desc limit 1",nativeQuery = true)
    GetReviewId findNextRecommendReviewId(@Param("createdAt") LocalDateTime createdAt, @Param("categoryScore") int categoryScore, @Param("reviewId") Long reviewId, @Param("categoryList") List<String> categoryList);

    @Query(value = "select SR.id,\n" +
            "       (select count(*) from ReviewKeep RK where RK.review_id = SR.id)'bookmarkCnt',\n" +
            "       S.name,\n" +
            "       SUM(CASE WHEN C.category IN (:categoryList) THEN 1 ELSE 0 END) AS categoryScore,\n" +
            "       SR.created_at,SR.img_url'imgUrl'\n" +
            "from StoreReview SR\n" +
            "         join Store S on S.id = SR.store_id\n" +
            "         join StoreCategory SC on S.id = SC.store_id\n" +
            "         join Category C on C.id = SC.category_id\n" +
            "where C.category IN (:categoryList) and SR.id !=:reviewId and SR.deleted = true or (SR.created_at = :createdAt AND SR.id > :reviewId)\n" +
            "group by SR.id\n" +
            "HAVING categoryScore >=:categoryScore "+
            "    and IF( categoryScore = :categoryScore , SR.id>:reviewId,1)=1 \n" +
            "order by categoryScore asc, SR.created_at asc,SR.id asc limit 1;\n",nativeQuery = true)
    GetReviewId findPrevRecommendReviewId(@Param("createdAt") LocalDateTime createdAt, @Param("categoryScore") int categoryScore, @Param("reviewId") Long reviewId, @Param("categoryList") List<String> categoryList);


    @Query(value = "select SR.id'reviewId',\n" +
                "       (select count(*) from ReviewKeep RK where RK.review_id = SR.id)   'bookmarkCnt'\n" +
                "        ,\n" +
                "       concat(R.region, ', ', R.city)                                    'regionInfo',\n" +
                "       S.name,\n" +
                "       SR.img_url                                                        'imgUrl',\n" +
                "       IF((select exists(select * from ReviewRead where ReviewRead.user_id = :userId and ReviewRead.review_id = SR.id)),\n" +
                "          'true', 'false')                                               'isRead',\n" +
                "       SUM(CASE WHEN C.category IN (:categoryList) THEN 1 ELSE 0 END) AS categoryScore,\n" +
                "       SR.created_at\n" +
                "from StoreReview SR\n" +
                "         join Store S on S.id = SR.store_id\n" +
                "         join StoreCategory SC on S.id = SC.store_id\n" +
                "         join Category C on C.id = SC.category_id\n" +
                "         join Region R on S.region_id = R.id\n" +
                "where C.category IN (:categoryList)\n" +
                "  and SR.deleted = true\n" +
                "group by SR.id\n" +
                "order by categoryScore DESC, SR.created_at desc,SR.id desc,\n" +
                "         (CASE WHEN ASCII(SUBSTRING(S.name, 1)) < 123 THEN 2 ELSE 1 END\n" +
                "             ), S.name \n",
                countQuery = "select count(DISTINCT StoreReview.id) from StoreReview join Store S on StoreReview.store_id = S.id" +
                        " join StoreCategory SC on S.id = SC.store_id join ReviewRead RR on RR.user_id=:userId and RR.review_id!=StoreReview.id  join Category C on SC.category_id = C.id" +
                        " where C.category IN(:categoryList) and StoreReview.status=true ",
                nativeQuery = true)
    Page<GetReview> RecommendReview(@Param("userId") Long userId, @Param("categoryList") List<String> categoryList, Pageable pageReq);

    interface GetReview {
        Long getReviewId();
        String getImgUrl();
        String getRegionInfo();
        Boolean getIsRead();

    }



    @Query(value = "select SR.id'reviewId', SR.img_url'imgUrl',concat(R.region,'/',R.city)'regionInfo',SR.created_at " +
            "from StoreReview SR " +
            "join ReviewKeep RK on SR.id = RK.review_id " +
            "join Store S on SR.store_id = S.id " +
            "join Region R on S.region_id = R.id " +
            "where RK.user_id=:userId and SR.deleted = true order by SR.created_at desc",countQuery = "select count(*) from StoreReview SR join ReviewKeep RK on SR.id = RK.review_id where RK.user_id=:userId",nativeQuery = true)
    Page<GetReviewInfo> getMyBookMarkReview(@Param("userId") Long userId, Pageable pageReq);


}
