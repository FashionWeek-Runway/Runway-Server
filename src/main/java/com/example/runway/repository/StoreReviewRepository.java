package com.example.runway.repository;

import com.example.runway.domain.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReviewRepository extends JpaRepository<StoreReview,Long> {
    Page<StoreReview> findByStoreIdAndStatusOrderByCreatedAtDesc(Long storeId,  boolean b,Pageable pageReq);
}
