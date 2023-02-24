package com.example.runway.repository;

import com.example.runway.domain.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview,Long> {
    Page<StoreReview> findByStoreIdAndStatusOrderByCreatedAtDesc(Long storeId,  boolean b,Pageable pageReq);

    @Query(nativeQuery = true,value="select SR.id         'reviewId',\n" +
            "       U.profile_url 'profileImgUrl',\n" +
            "       U.nickname,\n" +
            "       SR.img_url    'imgUrl',\n" +
            "       SR.store_id   'storeId',\n" +
            "       S.name        'storeName',\n" +
            "       concat(R.region,', ',R.city)'regionInfo'\n" +
            "from StoreReview SR\n" +
            "         join User U on U.id = SR.user_id\n" +
            "         join Store S on S.id = SR.store_id\n" +
            "         join Region R on R.id = S.region_id\n" +
            "where SR.id=:reviewId and SR.status=true")
    StoreReviewRepository.GetStoreReview getStoreReview(@Param("reviewId")Long reviewId);

    boolean existsByIdAndStatus(Long reviewId, boolean b);


    interface GetStoreReview {
        Long getReviewId();
        String getProfileImgUrl();
        String getNickname();
        String getImgUrl();
        Long getStoreId();
        String getStoreName();
        String getRegionInfo();
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
}
