package com.example.runway.repository;

import com.example.runway.domain.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview,Long> {
    Page<StoreReview> findByStoreIdAndStatusOrderByCreatedAtDescIdAsc(Long storeId,  boolean b,Pageable pageReq);

    @Query(nativeQuery = true,value="select SR.id         'reviewId',\n" +
            "       U.profile_url 'profileImgUrl',\n" +
            "       U.nickname,\n" +
            "       SR.img_url    'imgUrl',\n" +
            "       SR.store_id   'storeId',\n" +
            "       S.name        'storeName',\n" +
            "       concat(R.region,', ',R.city)'regionInfo'," +
            "       SR.created_at'createdAt'\n" +
            "from StoreReview SR\n" +
            "         join User U on U.id = SR.user_id\n" +
            "         join Store S on S.id = SR.store_id\n" +
            "         join Region R on R.id = S.region_id\n" +
            "where SR.id=:reviewId and SR.status=true")
    StoreReviewRepository.GetStoreReview getStoreReview(@Param("reviewId")Long reviewId);

    boolean existsByIdAndStatus(Long reviewId, boolean b);

    @Query(value = "select count(*)'size' from Store S join StoreCategory SC on S.id = SC.store_id " +
            "join Category C on SC.category_id = C.id join StoreReview SR on S.id = SR.store_id where C.category IN (:categoryList)",nativeQuery = true)
    StoreReviewRepository.GetCountAllReview CountReview(@Param(("categoryList")) List<String> categoryList);


    interface GetCountAllReview {
        int getSize();
    }
    @Query(value = "select SR.id \n" +
                "from StoreReview SR\n" +
                "where SR.store_id = :storeId\n" +
                "  and SR.created_at > :createdAt\n" +
                "   or (created_at = :createdAt AND id<:reviewId)\n" +
                "    and SR.id != :reviewId\n" +
                "order by created_at asc, SR.id desc limit 1",nativeQuery = true)
    StoreReviewRepository.GetReviewId findPrevReviewId(@Param("createdAt") LocalDateTime createdAt,@Param("storeId") Long storeId,@Param("reviewId") Long reviewId);


    @Query(value = "select SR.id\n" +
            "from StoreReview SR\n" +
            "where SR.store_id = :storeId\n" +
            "  and SR.created_at < :createdAt\n" +
            "   or (created_at = :createdAt AND id > :reviewId)\n" +
            "  and SR.id != :reviewId\n" +
            "order by created_at desc, SR.id desc limit 1",nativeQuery = true)
    StoreReviewRepository.GetReviewId findNextReviewId(@Param("createdAt") LocalDateTime createdAt, @Param("storeId") Long storeId, @Param("reviewId") Long reviewId);

    interface GetStoreReview {
        Long getReviewId();
        String getProfileImgUrl();
        String getNickname();
        String getImgUrl();
        Long getStoreId();
        String getStoreName();
        String getRegionInfo();
        LocalDateTime getCreatedAt();
    }

    @Query("SELECT DATE_FORMAT (SR.createdAt, '%Y/%m') AS date FROM StoreReview SR WHERE SR.user.id = :userId GROUP BY date order by date desc")
    List<String> findReviewDatesByUserId(@Param("userId") Long userId);

    @Query(value = "select SR.id 'reviewId', SR.img_url'imgUrl',concat(R.region,'/',R.city)'regionInfo'\n" +
            "from StoreReview SR\n" +
            "join Store S on SR.store_id = S.id\n" +
            "join Region R on S.region_id = R.id\n" +
            "where DATE_FORMAT (SR.created_at, '%Y/%m') = :date \n" +
            "  and SR.user_id = :userId",nativeQuery = true)
    List<StoreReviewRepository.GetReviewInfo> GetReviewInfo(@Param("date") String date,@Param("userId") Long userId);
    interface GetReviewInfo {
        Long getReviewId();
        String getImgUrl();
        String getRegionInfo();
    }


    interface GetReviewId{
        Long getId();
    }

    @Query(value =
            "select SR.id                          'reviewId',\n" +
                    "       SR.img_url                     'imgUrl',\n" +
                    "       concat(R.region, ', ', R.city) 'regionInfo',\n" +
                    "       IF((select exists(select * from ReviewRead where ReviewRead.user_id = :userId and ReviewRead.review_id = SR.id)),\n" +
                    "          'true', 'false')            'isRead',\n" +
                    "       count(K.user_id)'bookmarkCnt',S.name,\n" +
                    "       SUM(CASE WHEN C.category IN (:categoryList) THEN 1 ELSE 0 END) AS categoryScore\n" +
                    "from Store S\n" +
                    "         join StoreCategory SC on S.id = SC.store_id\n" +
                    "         join Category C on SC.category_id = C.id\n" +
                    "         join StoreReview SR on S.id = SR.store_id\n" +
                    "         join Region R on S.region_id = R.id\n" +
                    "         left join Keep K on S.id = K.store_id\n" +
                    "where C.category IN (:categoryList) and SR.status=true\n" +
                    "group by SR.id order by categoryScore DESC,bookmarkCnt DESC,SR.id asc",
            countQuery = "select count(DISTINCT StoreReview.id) from StoreReview join Store S on StoreReview.store_id = S.id" +
                        " join StoreCategory SC on S.id = SC.store_id join Category C on SC.category_id = C.id" +
                        " where C.category IN(:categoryList) ",
            nativeQuery = true)
    Page<GetReview> RecommendReview(@Param("userId") Long userId, @Param("categoryList") List<String> categoryList, Pageable pageReq);
    interface GetReview {
        Long getReviewId();
        String getImgUrl();
        String getRegionInfo();
        Boolean getIsRead();
    }
}
