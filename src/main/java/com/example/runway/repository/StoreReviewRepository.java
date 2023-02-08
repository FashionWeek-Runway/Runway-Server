package com.example.runway.repository;

import com.example.runway.domain.StoreReview;
import com.example.runway.dto.store.StoreRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview,Long> {
    Slice<StoreReview> findByStoreId(Long storeId, Pageable page);
}
