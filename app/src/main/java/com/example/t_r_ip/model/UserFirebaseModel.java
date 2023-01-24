package com.example.t_r_ip.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.t_r_ip.model.entities.User;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class UserFirebaseModel extends FirebaseModel {
    private static final UserFirebaseModel _instance = new UserFirebaseModel();

    private FirebaseModel firebaseModel;

    private UserFirebaseModel() {
        this.firebaseModel = FirebaseModel.instance();
    }

    public static UserFirebaseModel instance(){
        return _instance;
    }


    public void saveUser (User user, Model.Listener<Void> listener) {
        // refresh?
        firebaseModel.getDb().collection(User.COLLECTION).document(user.getId()).set(user.toJson())
                .addOnCompleteListener(task -> { listener.onComplete(null); });
    }

    public void getUserDataById (String id, Model.Listener<User> listener) {
        firebaseModel.getDb().collection(User.COLLECTION).document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null) {
                               Map<String, Object> document = task.getResult().getData();
                               User user = User.fromJson(document);
                               listener.onComplete(user);
                           }
                       }
                   }
                );
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        StorageReference storageRef = firebaseModel.getStorage().getReference();
        StorageReference imagesRef = storageRef.child("users/" + name + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TAG", "URL: " + uri.toString());
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }

}
