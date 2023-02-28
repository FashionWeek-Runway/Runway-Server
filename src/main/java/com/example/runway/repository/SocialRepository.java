package com.example.runway.repository;


import com.example.runway.domain.Social;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialRepository extends JpaRepository<Social, Long> {
    boolean existsByUserIdAndType(Long userId, String apple);

    Optional<Social> findBySocialIdAndType(String appleId, String apple);

    boolean existsBySocialIdAndUserIdNot(String socialId, Long userId);
}
