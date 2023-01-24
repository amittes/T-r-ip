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
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static Model instance(){
        return _instance;
    }

    private Model() {
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getCurrentUserId() {
        return mAuth.getCurrentUser().getUid();
    }

    public void saveUser (User user, Model.Listener<Void> listener) {
        firebaseModel.saveUser(user,listener); // refresh?
    }

    public void getUserDataById (String id, Listener<User> listener) {
        firebaseModel.getUserById(id, user -> listener.onComplete(user));
    }
//
//    public void updateUserDisplayName(String displayName) {
//        UserProfileChangeRequest.Builder userProfileChangeRequestBuilder = new UserProfileChangeRequest.Builder();
//        userProfileChangeRequestBuilder.setDisplayName(displayName);
//        getCurrentUser().updateProfile(userProfileChangeRequestBuilder.build());
//    }

    public void updateUserPassword(String password) {
        getCurrentUser().updatePassword(password);
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.saveImageInStorage(name,bitmap,listener);
    }

    public void logOut () {
        mAuth.signOut();
    }
}
