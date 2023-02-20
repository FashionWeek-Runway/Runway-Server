package com.example.runway.repository;


import com.example.runway.domain.Keep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface KeepRepository extends JpaRepository<Keep,Long> {

    boolean existsByUserIdAndStoreId(Long id, Long storeId);

    @Transactional
    void deleteByUserIdAndStoreId(Long userId, Long storeId);
}
