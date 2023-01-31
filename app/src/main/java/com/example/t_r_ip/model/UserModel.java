package com.example.t_r_ip.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.t_r_ip.model.entities.User;

public class UserModel {
    private static final UserModel _instance = new UserModel();

    private final Model model;
    private final UserFirebaseModel userFirebaseModel;
    private User currentUser;

    private UserModel() {
        this.model = Model.instance();
        this.userFirebaseModel = UserFirebaseModel.instance();
    }

    public static UserModel instance() {
        return _instance;
    }

    public boolean isUserConnected() {
        if (Model.instance().mAuth.getCurrentUser() == null) {
            return false;
        }
        return true;
    }

    public String getCurrentUserEmail() {
        return model.mAuth.getCurrentUser().getEmail();
    }

    public String getCurrentUserId() {
        return model.mAuth.getCurrentUser().getUid();
    }

    public void saveUser(User user, Model.Listener<Void> listener) {
        userFirebaseModel.saveUser(user, listener);
        this.currentUser = user;
    }

    public void getUserDataById(String id, Model.Listener<User> listener) {
        userFirebaseModel.getUserDataById(id, listener);
    }

    public void updateUserPassword(String password) {
        model.mAuth.getCurrentUser().updatePassword(password);
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        userFirebaseModel.uploadImage(name, bitmap, listener);
    }

    public void logOut() {
        model.mAuth.signOut();
        this.currentUser = null;
    }

    public void logIn() {
        this.userFirebaseModel.getUserDataById(getCurrentUserId(), user -> {
            this.currentUser = user;
        });
    }
}
