package com.example.t_r_ip.model;

import android.graphics.Bitmap;

import com.example.t_r_ip.model.entities.User;
import com.google.firebase.auth.FirebaseUser;

public class UserModel {
    private static final UserModel _instance = new UserModel();

    private Model model;
    private UserFirebaseModel userFirebaseModel;

    private UserModel() {
        this.model = Model.instance();
        this.userFirebaseModel = UserFirebaseModel.instance();
    }

    public static UserModel instance() {
        return _instance;
    }

    public FirebaseUser getCurrentUser() {
        return model.mAuth.getCurrentUser();
    }

    public String getCurrentUserId() {
        return getCurrentUser().getUid();
    }

    public void saveUser(User user, Model.Listener<Void> listener) {
        userFirebaseModel.saveUser(user, listener);
    }

    public void getUserDataById(String id, Model.Listener<User> listener) {
        userFirebaseModel.getUserDataById(id, listener);
    }

    public void updateUserPassword(String password) {
        getCurrentUser().updatePassword(password);
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        userFirebaseModel.uploadImage(name, bitmap, listener);
    }

    public void logOut() {
        model.mAuth.signOut();
    }
}
