package com.example.t_r_ip.model.entities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.t_r_ip.MyApplication;
import com.google.gson.annotations.Expose;

import java.util.UUID;

@Entity
public class Post {

    public static final String COLLECTION = "posts";
    public static final String LAST_UPDATED = "lastUpdated";
    private static final String LOCAL_LAST_UPDATED = "posts_local_last_update";
    @PrimaryKey
    @NonNull
    @Expose
    private String id;
    @NonNull
    @Expose
    private String authorId;
    @NonNull
    @Expose
    private String postText;
    @NonNull
    @Expose
    private String postPictureUrl;
    @NonNull
    @Expose
    private String location;
    @NonNull
    @Expose
    private boolean isDeleted;
    @NonNull
    @Expose(deserialize = false)
    private long lastUpdated;

    public Post() {
        this.id = String.valueOf(UUID.randomUUID());
        this.authorId = "";
        this.postText = "";
        this.postPictureUrl = "";
        this.location = "";
        this.isDeleted = false;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED, time);
        editor.commit();
    }

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorEmail) {
        this.authorId = authorEmail;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
