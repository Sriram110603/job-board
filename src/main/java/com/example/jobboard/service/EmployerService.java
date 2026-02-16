package com.example.jobboard.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.jobboard.entity.Employer;
import com.example.jobboard.repository.EmployerRepository;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployerService(EmployerRepository employerRepository,
                           PasswordEncoder passwordEncoder) {
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(Employer employer) {

        if (employerRepository.existsByEmail(employer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        employer.setPassword(passwordEncoder.encode(employer.getPassword()));
        employerRepository.save(employer);
    }
}

