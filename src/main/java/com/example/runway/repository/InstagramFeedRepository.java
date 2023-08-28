package com.example.runway.repository;

import com.example.runway.domain.InstagramFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramFeedRepository extends JpaRepository<InstagramFeed,Long> {
    @EntityGraph(attributePaths = "instagramImg")
    Page<InstagramFeed> findAllByOrderByCreatedAtDesc(Pageable pageReq);
}
