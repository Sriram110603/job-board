package com.example.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jobboard.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
