package com.example.jobboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jobboard.entity.Employer;
import com.example.jobboard.entity.Job;
import com.example.jobboard.entity.JobApplication;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.EmployerRepository;
import com.example.jobboard.repository.JobApplicationRepository;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.repository.UserRepository;

@Controller
public class DashboardController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        boolean isEmployer = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"));

        // ================= EMPLOYER DASHBOARD =================
        if (isEmployer) {

            String email = auth.getName();

            Employer employer = employerRepository
                    .findByEmail(email)
                    .orElseThrow();

            // Employer posted jobs
            List<Job> jobs = jobRepository
                    .findByCompany(employer.getCompanyName());

            // Applications for those jobs
            List<JobApplication> applications =
                    jobApplicationRepository.findByJobIn(jobs);

            // All candidate profiles
            List<User> candidates = userRepository.findAll();

            model.addAttribute("applications", applications);
            model.addAttribute("candidates", candidates);
            model.addAttribute("employerName",
                    employer.getCompanyName());

            return "employer-dashboard";
        }

        // ================= EMPLOYEE DASHBOARD =================

        List<Job> jobs = jobRepository.findAll();

        model.addAttribute("jobs", jobs);

        return "employee-dashboard";
    }

    // ================= EMPLOYER PROFILE =================

    @GetMapping("/employer/profile")
    public String employerProfile(Authentication auth,
                                  Model model) {

        Employer employer = employerRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        model.addAttribute("employer", employer);

        return "employer-profile";
    }

    // ================= UPDATE EMPLOYER PROFILE =================

    @PostMapping("/employer/profile")
    public String updateEmployerProfile(
            @RequestParam String companyName,
            @RequestParam String phone,
            @RequestParam String companySize,
            Authentication auth) {

        Employer employer = employerRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        employer.setCompanyName(companyName);
        employer.setPhone(phone);
        employer.setCompanySize(companySize);

        employerRepository.save(employer);

        return "redirect:/employer/profile";
    }
}
