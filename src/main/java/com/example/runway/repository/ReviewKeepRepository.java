package com.example.runway.repository;


import com.example.runway.domain.ReviewKeep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewKeepRepository extends JpaRepository<ReviewKeep,Long> {

    boolean existsByIdUserIdAndIdReviewId(Long id, Long reviewId);

    @Transactional
    void deleteByIdUserIdAndIdReviewId(Long userId, Long reviewId);
}
