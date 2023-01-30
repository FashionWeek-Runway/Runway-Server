package com.example.imenu_spring_project.repository;


import com.example.imenu_spring_project.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}