package com.example.jobboard.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jobboard.entity.Employer;
import com.example.jobboard.entity.User;
import com.example.jobboard.repository.EmployerRepository;
import com.example.jobboard.repository.JobRepository;
import com.example.jobboard.service.EmployerService;
import com.example.jobboard.service.UserService;

@Controller
public class AuthController {
	@Autowired
	private PasswordEncoder passwordEncoder;
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
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

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
    // Forgot Password
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {

        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Email not found");
            return "forgot-password";
        }

        model.addAttribute("email", email);
        return "reset-password";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password) {

        User user = userService.findByEmail(email);

        if (user == null) {
            return "redirect:/forgot-password";
        }

        user.setPassword(passwordEncoder.encode(password));

        userService.save(user);

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
    @GetMapping("/employer/login")
    public String showEmployerLogin() {
        return "employer-login";
    }

}