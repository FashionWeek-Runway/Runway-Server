package com.example.runway.repository;

import com.example.runway.domain.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            "where SR.id=:reviewId")
    StoreReviewRepository.GetStoreReview getStoreReview(@Param("reviewId")Long reviewId);
    interface GetStoreReview {
        Long getReviewId();
        String getProfileImgUrl();
        String getNickname();
        String getImgUrl();
        Long getStoreId();
        String getStoreName();
        String getRegionInfo();
    }
}
