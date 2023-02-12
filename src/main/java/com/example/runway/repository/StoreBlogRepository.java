package com.example.runway.repository;

import com.example.runway.domain.StoreBlog;
import com.example.runway.domain.StoreReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBlogRepository extends JpaRepository<StoreBlog,Long> {
}
