package com.example.runway.repository;

import com.example.runway.domain.Category;
import com.example.runway.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
}
