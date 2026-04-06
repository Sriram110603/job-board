package com.example.jobboard.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.jobboard.entity.*;
import com.example.jobboard.repository.*;

@Controller
public class ApplicationController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @PostMapping("/apply")
    public String apply(@RequestParam Long jobId, Authentication auth) {

        System.out.println("APPLY BUTTON CLICKED");

        Job job = jobRepository.findById(jobId).orElseThrow();

        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        // 🔥 FIX: check BEFORE saving
        boolean alreadyApplied = jobApplicationRepository
                .existsByJobAndUser(job, user);

        if (alreadyApplied) {
            return "redirect:/apply-success";
        }

        JobApplication app = new JobApplication();
        app.setJob(job);
        app.setUser(user);
        app.setAppliedDate(LocalDate.now());
        app.setStatus("APPLIED");

        jobApplicationRepository.save(app);

        return "redirect:/apply-success";
    }

    @GetMapping("/apply-success")
    public String successPage() {
        return "apply-success";
    }
}