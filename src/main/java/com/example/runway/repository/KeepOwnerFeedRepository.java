package com.example.runway.repository;

import com.example.runway.domain.KeepOwnerFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface KeepOwnerFeedRepository extends JpaRepository<KeepOwnerFeed,Long> {

    boolean existsByUserIdAndFeedId(Long userId, Long feedId);

    @Transactional
    void deleteByUserIdAndFeedId(Long userId, Long feedId);
}
