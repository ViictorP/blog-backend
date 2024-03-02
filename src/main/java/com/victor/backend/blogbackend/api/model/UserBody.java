package com.victor.backend.blogbackend.api.model;

import com.victor.backend.blogbackend.model.LocalUser;

import java.time.LocalDateTime;

public class UserBody {

    private String username;
    private String bio;
    private LocalDateTime registrationDate;

    public UserBody(LocalUser user) {
        username = user.getUsername();
        bio = user.getBiography();
        registrationDate = user.getRegistrationDate();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
