package com.example.runway.repository;

import com.example.runway.domain.User;
import com.example.runway.domain.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
}
