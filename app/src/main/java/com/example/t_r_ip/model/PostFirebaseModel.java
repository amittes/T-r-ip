package com.example.t_r_ip.model;

import static com.example.t_r_ip.model.entities.Post.LAST_UPDATED;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.t_r_ip.model.entities.Post;
import com.example.t_r_ip.model.utils.ImageUploader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostFirebaseModel extends FirebaseModel{
    private static final PostFirebaseModel _instance = new PostFirebaseModel();

    private FirebaseModel firebaseModel;
    private Gson gson;

    private PostFirebaseModel() {
        this.firebaseModel = FirebaseModel.instance();
        this.gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public static PostFirebaseModel instance(){
        return _instance;
    }

    public void getAllPostsSince(Long since, PostModel.Listener<List<Post>> callback){
        firebaseModel.getDb().collection(Post.COLLECTION)
                .whereGreaterThanOrEqualTo(LAST_UPDATED, new Timestamp(since,0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot snapshot: jsonsList){
                                String json = gson.toJson(snapshot.getData());
                                Post post = gson.fromJson(json, Post.class);
                                Timestamp time = (Timestamp) snapshot.get(LAST_UPDATED);
                                post.setLastUpdated(time.getSeconds());
                                list.add(post);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }

    public void addPost(Post post, PostModel.Listener<Void> listener) {
        String jsonPost = gson.toJson(post);
        Map<String, Object> mapPost = gson.fromJson(jsonPost, new TypeToken<Map<String, Object>>(){}.getType());
        mapPost.put(LAST_UPDATED, FieldValue.serverTimestamp());
        firebaseModel.getDb().collection(Post.COLLECTION).document(post.getId()).set(mapPost)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(null);
                    }
                });
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        ImageUploader imageUploader = new ImageUploader(firebaseModel, "posts", name, bitmap, listener);
        imageUploader.upload();
    }
}
