package com.example.jobboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobboard.entity.Employer;
import com.example.jobboard.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompany(String company); // quick hack (works now)
   
}