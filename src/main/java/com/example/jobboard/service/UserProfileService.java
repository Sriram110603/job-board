package com.example.jobboard.service;

import org.springframework.stereotype.Service;
import com.example.jobboard.entity.User;
import com.example.jobboard.entity.UserProfile;
import com.example.jobboard.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfile saveProfile(UserProfile profile) {

        int completion = calculateCompletion(profile);
        profile.setProfileCompletion(completion);

        return repository.save(profile);
    }

    public UserProfile getProfileByUser(User user) {
        return repository.findByUser(user);
    }

    private int calculateCompletion(UserProfile profile) {

        int fields = 0;
        int filled = 0;

        fields++; if(profile.getPrimarySkills()!=null) filled++;
        fields++; if(profile.getSecondarySkills()!=null) filled++;
        fields++; if(profile.getCity()!=null) filled++;
        fields++; if(profile.getState()!=null) filled++;
        fields++; if(profile.getCountry()!=null) filled++;
        fields++; if(profile.getPreferredLocations()!=null) filled++;

        return (filled * 100) / fields;
    }
}