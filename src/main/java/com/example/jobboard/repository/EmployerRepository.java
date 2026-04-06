package com.example.jobboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobboard.entity.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

    boolean existsByEmail(String email);

    Optional<Employer> findByEmail(String email); // 🔥 THIS is required
}