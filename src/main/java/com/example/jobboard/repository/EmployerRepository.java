package com.example.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jobboard.entity.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

    boolean existsByEmail(String email);

}
