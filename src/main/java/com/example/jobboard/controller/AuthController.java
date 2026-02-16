package com.example.jobboard.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

import com.example.jobboard.entity.Employer;
import com.example.jobboard.entity.User;
import com.example.jobboard.service.EmployerService;
import com.example.jobboard.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;
    private final EmployerService employerService;

    // Constructor Injection (clean and required)
    public AuthController(UserService userService,
                          EmployerService employerService) {
        this.userService = userService;
        this.employerService = employerService;
    }

    // ====================================================
    // LANDING PAGE
    // ====================================================
    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

    // ====================================================
    // UNIFIED LOGIN PAGE (FOR BOTH ROLES)
    // ====================================================
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // ====================================================
    // DASHBOARD ROUTING BASED ON ROLE
    // ====================================================
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"))) {

            return "employer-dashboard";
        }

        return "employee-dashboard";
    }

    // ====================================================
    // EMPLOYEE REGISTRATION
    // ====================================================
    @GetMapping("/register")
    public String showEmployeeRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processEmployeeRegistration(
            @Valid @ModelAttribute("user") User user,
            BindingResult result) {

        if (result.hasErrors()) {
            return "register";
        }

        userService.register(user);

        return "redirect:/login";
    }

    // ====================================================
    // EMPLOYER REGISTRATION
    // ====================================================
    @GetMapping("/employer/register")
    public String showEmployerRegisterForm(Model model) {
        model.addAttribute("employer", new Employer());
        return "employer-register";
    }

    @PostMapping("/employer/register")
    public String processEmployerRegistration(
            @Valid @ModelAttribute("employer") Employer employer,
            BindingResult result) {

        if (result.hasErrors()) {
            return "employer-register";
        }

        employerService.register(employer);

        return "redirect:/login";
    }
}