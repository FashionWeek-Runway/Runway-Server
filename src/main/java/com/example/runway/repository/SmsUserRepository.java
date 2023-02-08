package com.example.runway.repository;

import com.example.runway.domain.SmsUser;
import com.example.runway.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SmsUserRepository extends JpaRepository<SmsUser, Long> {

    int countByPhoneAndCreatedAtGreaterThan(String to, LocalDateTime dateTime);
}
