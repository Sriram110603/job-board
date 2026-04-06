package com.example.jobboard.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.jobboard.entity.Employer;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.EmployerRepository;
import com.example.jobboard.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;

    public CustomUserDetailsService(UserRepository userRepository,
                                    EmployerRepository employerRepository) {
        this.userRepository = userRepository;
        this.employerRepository = employerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 🔹 Check USER
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority(user.getRole()))
            );
        }

        // 🔹 Check EMPLOYER
        Optional<Employer> empOpt = employerRepository.findByEmail(email);
        if (empOpt.isPresent()) {
            Employer emp = empOpt.get();

            return new org.springframework.security.core.userdetails.User(
                    emp.getEmail(),
                    emp.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_EMPLOYER"))
            );
        }

        throw new UsernameNotFoundException("User not found");
    }
}
