package com.example.jobboard.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.Employer;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.repository.EmployerRepository;

@Controller
@RequestMapping("/employer")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @PostMapping("/post-job")
    public String postJob(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam String description,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String experience,
            Authentication authentication
    ) {

        Employer employer = employerRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job = new Job();
        job.setTitle(title);
        job.setLocation(location);
        job.setDescription(description);
        job.setExperience(experience);
        job.setCompany(employer.getCompanyName());
        job.setPostedDate(LocalDate.now());

        jobRepository.save(job);

        return "redirect:/dashboard";
    }
}