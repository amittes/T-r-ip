package com.example.t_r_ip.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class User {
    private FirebaseAuth firebaseAuthModel;
//TODO: for future refactoring
    public User() {
        this.firebaseAuthModel = Model.instance().mAuth;
    }
//    public FirebaseUser getCurrentUser() {
//        return firebaseAuthModel.getCurrentUser();
//    }
//
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
//        firebaseAuthModel.signOut();
//    }
}
