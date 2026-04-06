package com.example.jobboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.jobboard.entity.Employer;
import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.JobApplication;
import com.example.jobboard.repository.EmployerRepository;
import com.example.jobboard.repository.JobApplicationRepository;
import com.example.jobboard.repository.JobRepository;

@Controller
public class DashboardController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        boolean isEmployer = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"));

        if (isEmployer) {

            String email = auth.getName();

            Employer employer = employerRepository
                    .findByEmail(email)
                    .orElseThrow();

            List<Job> jobs = jobRepository.findByCompany(employer.getCompanyName());

            // 🔥 THIS IS WHAT YOU WERE MISSING
            List<JobApplication> applications =
                    jobApplicationRepository.findByJobIn(jobs);

            model.addAttribute("applications", applications);

            return "employer-dashboard";
        }

        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);

        return "employee-dashboard";
    }
    }
