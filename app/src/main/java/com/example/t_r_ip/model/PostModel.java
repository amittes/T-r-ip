package com.example.t_r_ip.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.t_r_ip.model.entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostModel {
    private FirebaseModel firebaseModel;
    private Gson gson;

    public PostModel() {
        this.firebaseModel = FirebaseModel.instance();
        this.gson = new Gson();
    }

    public void getAllPosts(Long since, Model.Listener<List<Post>> callback){
        firebaseModel.getDb().collection(Post.COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Post post = gson.fromJson(String.valueOf(json), Post.class);
                                list.add(post);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }

    public void addPost(Post post, Model.Listener<Void> listener) {
        String jsonPost = gson.toJson(post);
        Map<String, Object> mapPost = gson.fromJson(jsonPost, new TypeToken<Map<String, Object>>(){}.getType());

        firebaseModel.getDb().collection(Post.COLLECTION).document(post.getId()).set(mapPost)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(null);
                    }
                });
    }

//    void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener){
//        StorageReference storageRef = firebaseModel.getStorage().getReference();
//        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imagesRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                listener.onComplete(null);
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        listener.onComplete(uri.toString());
//                    }
//                });
//            }
//        });
//    }
}
