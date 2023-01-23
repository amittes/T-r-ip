package com.example.t_r_ip.model.entities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.t_r_ip.MyApplication;
import com.google.firebase.Timestamp;
import com.google.gson.annotations.Expose;

import java.util.UUID;

@Entity
public class Post {

    @PrimaryKey
    @NonNull
    @Expose
    private String id;
    @Expose
    private String authorEmail;
    @Expose
    private String displayName;
    @Expose
    private String authorPictureUrl;
    @Expose
    private String postText;
    @Expose
    private String postPictureUrl;
    @Expose(deserialize = false)
    private long lastUpdated;
    public static final String COLLECTION = "posts";
    public static final String LAST_UPDATED = "lastUpdated";
    private static final String LOCAL_LAST_UPDATED = "students_local_last_update";

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

    public void setId(@NonNull String id) {
        this.id = id;
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

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.commit();
    }
}
