package com.example.t_r_ip.model;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static Model instance(){
        return _instance;
    }

    private Model() {
    }

//    public FirebaseUser getCurrentUser() {
//        return mAuth.getCurrentUser();
//    }

//    public void updateUserDisplayName(String displayName) {
//        UserProfileChangeRequest.Builder userProfileChangeRequestBuilder = new UserProfileChangeRequest.Builder();
//        userProfileChangeRequestBuilder.setDisplayName(displayName);
//        getCurrentUser().updateProfile(userProfileChangeRequestBuilder.build());
//    }
//
//    public void updateUserPassword(String password) {
//        getCurrentUser().updatePassword(password);
//    }
//
//    public void updateUserProfilePicture(Uri uri) {
//        UserProfileChangeRequest.Builder userProfileChangeRequestBuilder = new UserProfileChangeRequest.Builder();
//        userProfileChangeRequestBuilder.setPhotoUri(uri);
//        getCurrentUser().updateProfile(userProfileChangeRequestBuilder.build());
//    }
//
//    public void logOut () {
//        mAuth.signOut();
//    }
//
//    public interface Listener<T> {
//        void onComplete(T list);
//    }
}
