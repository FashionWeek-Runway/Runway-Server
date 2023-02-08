package com.example.runway.repository;


import com.example.runway.domain.Keep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeepRepository extends JpaRepository<Keep,Long> {

    boolean existsByUserIdAndStoreId(Long id, Long storeId);
}
