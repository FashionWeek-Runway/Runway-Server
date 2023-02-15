package com.example.runway.repository;

import com.example.runway.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByRegionContaining(String content);


}
