package com.example.t_r_ip.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.t_r_ip.model.entities.User;
import com.example.t_r_ip.model.utils.ImageUploader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.Map;

public class UserFirebaseModel extends FirebaseModel {
    private static final UserFirebaseModel _instance = new UserFirebaseModel();

    private FirebaseModel firebaseModel;
    private Gson gson;

    private UserFirebaseModel() {
        this.firebaseModel = FirebaseModel.instance();
        this.gson = new Gson();
    }

    public static UserFirebaseModel instance() {
        return _instance;
    }


    public void saveUser(User user, Model.Listener<Void> listener) {
        String userJson = gson.toJson(user);
        Map<String, Object> mapUser = gson.fromJson(userJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        firebaseModel.getDb().collection(User.COLLECTION).document(user.getId()).set(mapUser)
                .addOnCompleteListener(task -> {
                    listener.onComplete(null);
                });
    }

    public void getUserDataById(String id, Model.Listener<User> listener) {
        firebaseModel.getDb().collection(User.COLLECTION).document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null) {
                                                   Map<String, Object> document = task.getResult().getData();
                                                   String json = gson.toJson(document);
                                                   User user = gson.fromJson(json, User.class);
                                                   listener.onComplete(user);
                                               }
                                           }
                                       }
                );
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        ImageUploader imageUploader = new ImageUploader(firebaseModel, "users", name, bitmap, listener);
        imageUploader.upload();
    }

}
