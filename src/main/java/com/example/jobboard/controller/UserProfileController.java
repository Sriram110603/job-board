package com.example.jobboard.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.jobboard.entity.User;
import com.example.jobboard.entity.UserProfile;
import com.example.jobboard.repository.UserRepository;
import com.example.jobboard.service.UserProfileService;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserProfileService profileService;
    private final UserRepository userRepository;

    public UserProfileController(UserProfileService profileService,
                                 UserRepository userRepository) {
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showProfile(Model model, Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = profileService.getProfileByUser(user);

        if (profile == null) {
            profile = new UserProfile();
        }

        model.addAttribute("profile", profile);

        return "user-profile";
    }

    @PostMapping
    public String saveProfile(@ModelAttribute UserProfile profile,
                              Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setUser(user);

        profileService.saveProfile(profile);

        return "employee-dashboard";

    }
}