package com.example.runway.repository;

import com.example.runway.domain.ReviewRead;
import com.example.runway.domain.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportRepository extends JpaRepository<ReviewReport,Long> {
}
