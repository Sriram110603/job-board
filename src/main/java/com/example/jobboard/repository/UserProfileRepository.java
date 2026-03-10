package com.example.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jobboard.entity.User;
import com.example.jobboard.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findByUser(User user);
}
