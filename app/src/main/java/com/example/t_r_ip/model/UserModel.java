package com.example.t_r_ip.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserModel {
    private static final UserModel _instance = new UserModel();

    private Model model;

    private UserModel() {
        this.model = Model.instance();
    }

    public static UserModel instance(){
        return _instance;
    }

    public FirebaseUser getCurrentUser() {
        return model.mAuth.getCurrentUser();
    }

    public void updateUserDisplayName(String displayName) {
        UserProfileChangeRequest.Builder userProfileChangeRequestBuilder = new UserProfileChangeRequest.Builder();
        userProfileChangeRequestBuilder.setDisplayName(displayName);
        getCurrentUser().updateProfile(userProfileChangeRequestBuilder.build());
    }

    public void updateUserPassword(String password) {
        getCurrentUser().updatePassword(password);
    }

    public void updateUserProfilePicture(Uri uri) {
        UserProfileChangeRequest.Builder userProfileChangeRequestBuilder = new UserProfileChangeRequest.Builder();
        userProfileChangeRequestBuilder.setPhotoUri(uri);
        getCurrentUser().updateProfile(userProfileChangeRequestBuilder.build());
    }

    public void logOut () {
        model.mAuth.signOut();
    }

    public interface Listener<T> {
        void onComplete(T list);
    }
}
