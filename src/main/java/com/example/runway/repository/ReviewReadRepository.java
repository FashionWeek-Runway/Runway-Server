package com.example.runway.repository;

import com.example.runway.domain.ReviewRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewReadRepository extends JpaRepository<ReviewRead,Long> {

    boolean existsByIdReviewIdAndIdUserId(Long reviewId, Long userId);

    ReviewRead findByIdReviewIdAndIdUserId(Long reviewId, Long userId);

    @Transactional
    void deleteByIdReviewId(Long reviewId);
}
