package com.example.jobboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.JobApplication;
import com.example.jobboard.entity.User;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // 🔥 THIS is what your controller is trying to call
    List<JobApplication> findByJobIn(List<Job> jobs);
    boolean existsByJobAndUser(Job job, User user);
}