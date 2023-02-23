package com.example.runway.repository;

import com.example.runway.domain.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> findByUserIdAndStatus(Long userId,boolean status);

    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);

    Optional<UserCategory> findByUserIdAndCategoryId(Long userId, Long aLong);

    @Transactional
    void deleteByUserId(Long userId);

    List<UserCategory> findByUserId(Long userId);
}
