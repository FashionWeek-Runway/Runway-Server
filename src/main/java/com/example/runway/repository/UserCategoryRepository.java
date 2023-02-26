package com.example.runway.repository;

import com.example.runway.domain.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> findByIdUserIdAndStatus(Long userId,boolean status);

    @Transactional
    void deleteByIdUserId(Long userId);

}
