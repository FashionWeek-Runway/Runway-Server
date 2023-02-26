package com.example.runway.repository;

import com.example.runway.domain.ReviewRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReadRepository extends JpaRepository<ReviewRead,Long> {

    boolean existsByIdReviewIdAndIdUserId(Long reviewId, Long userId);

    ReviewRead findByIdReviewIdAndIdUserId(Long reviewId, Long userId);
}
