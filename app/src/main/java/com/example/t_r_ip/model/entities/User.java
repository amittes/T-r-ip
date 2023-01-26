package com.example.t_r_ip.model.entities;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String COLLECTION = "users";
    static final String DISPLAY_NAME = "displayName";
    static final String ID = "id";
    static final String EMAIL = "email";
    static final String PROFILE_PICTURE_URL = "profilePictureUrl";
    private String id;
    private String email;
    private String displayName;
    private String profilePictureUrl;

    public User(String id, String email, String displayName, String profilePictureUrl) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public static User fromJson(@NonNull Map<String, Object> json) {
        String id = (String) json.get(ID);
        String email = (String) json.get(EMAIL);
        String displayName = (String) json.get(DISPLAY_NAME);
        String profilePictureUrl = (String) json.get(PROFILE_PICTURE_URL);
        User user = new User(id, email, displayName, profilePictureUrl);

        return user;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(EMAIL, getEmail());
        json.put(DISPLAY_NAME, getDisplayName());
        json.put(PROFILE_PICTURE_URL, getProfilePictureUrl());
        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

}