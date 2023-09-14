package com.example.runway.repository;

import com.example.runway.domain.StoreScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreScrapRepository extends JpaRepository<StoreScrap, Long> {
    List<StoreScrap> findByStoreId(Long storeId);
}
