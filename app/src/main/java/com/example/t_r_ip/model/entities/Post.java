package com.example.t_r_ip.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Post {

    @PrimaryKey
    @NonNull
    private String id;

    private String authorEmail;
    private String displayName;
    private String authorPictureUrl;
    private String postText;
    private String postPictureUrl;
    public static final String COLLECTION = "posts";

    public Post() {
        this.id = String.valueOf(UUID.randomUUID());
        this.authorEmail = "";
        this.displayName = "";
        this.authorPictureUrl = "";
        this.postText = "";
        this.postPictureUrl = "";
    }

    public String getId() {
        return id;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAuthorPictureUrl() {
        return authorPictureUrl;
    }

    public void setAuthorPictureUrl(String authorPictureUrl) {
        this.authorPictureUrl = authorPictureUrl;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostPictureUrl() {
        return postPictureUrl;
    }

    public void setPostPictureUrl(String postUrl) {
        this.postPictureUrl = postUrl;
    }
}
