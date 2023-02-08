package com.example.runway.repository;

import com.example.runway.domain.StoreImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreImgRepository extends JpaRepository<StoreImg,Long> {
    List<StoreImg> findByStoreId(Long storeId);
}
