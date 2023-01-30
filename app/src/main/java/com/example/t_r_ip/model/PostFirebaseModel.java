package com.example.t_r_ip.model;

import static com.example.t_r_ip.model.entities.Post.LAST_UPDATED;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.t_r_ip.model.entities.Post;
import com.example.t_r_ip.model.utils.ImageUploader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostFirebaseModel extends FirebaseModel {
    private static final PostFirebaseModel _instance = new PostFirebaseModel();

    private FirebaseModel firebaseModel;
    private Gson gson;

    private PostFirebaseModel() {
        this.firebaseModel = FirebaseModel.instance();
        this.gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public static PostFirebaseModel instance() {
        return _instance;
    }

    public void getAllPostsSince(Long since, PostModel.Listener<List<Post>> callback) {
        firebaseModel.getDb().collection(Post.COLLECTION)
                .whereGreaterThanOrEqualTo(LAST_UPDATED, new Timestamp(since, 0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()) {
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot snapshot : jsonsList) {
                                String json = gson.toJson(snapshot.getData());
                                Post post = gson.fromJson(json, Post.class);
                                Timestamp time = (Timestamp) snapshot.get(LAST_UPDATED);
                                post.setLastUpdated(time.getSeconds());
                                list.add(post);
                            }
                        }
                        Log.d("TAG", "receive " + list.size() + " documents from firebase");
                        callback.onComplete(list);
                    }
                });
    }

    public void getAllUserPostsSince(String id, Long since, PostModel.Listener<List<Post>> callback) {
        firebaseModel.getDb().collection(Post.COLLECTION)
                .whereEqualTo("authorId", id)
                .whereGreaterThanOrEqualTo(LAST_UPDATED, new Timestamp(since, 0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> list = new LinkedList<>();
                        if (task.isSuccessful()) {
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot snapshot : jsonsList) {
                                String json = gson.toJson(snapshot.getData());
                                Post post = gson.fromJson(json, Post.class);
                                Timestamp time = (Timestamp) snapshot.get(LAST_UPDATED);
                                post.setLastUpdated(time.getSeconds());
                                if(!post.isDeleted()) {
                                    list.add(post);
                                }
                            }
                        }
                        Log.d("TAL", "receive " + list.size() + " documents from firebase to user posts");
                        callback.onComplete(list);
                    }
                });
    }

    public void getPostByIdSince(String id, Long since, PostModel.Listener<Post> callback) {
        Log.d("TAG", "getPostByIdSince " + id);
        Timestamp t = new Timestamp(since, 0);
        Log.d("TAG", "getAllUserPostsSince LAST_UPDATED " + t.toString());
        firebaseModel.getDb().collection(Post.COLLECTION)
                .whereEqualTo("id", id)
                .whereGreaterThanOrEqualTo(LAST_UPDATED, new Timestamp(since, 0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Post post = new Post();
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot  = task.getResult();
                            String json = gson.toJson(snapshot.getDocuments().get(0));
                            post = gson.fromJson(json, Post.class);
                            Timestamp time = (Timestamp) snapshot.getDocuments().get(0).get(LAST_UPDATED);
                            post.setLastUpdated(time.getSeconds());
                            Log.d("TAG", "Successful task, post id:" + post.getId());
                        }
                        callback.onComplete(post);
                    }
                });


    }

    public void addPost(Post post, PostModel.Listener<Void> listener) {
        String jsonPost = gson.toJson(post);
        Map<String, Object> mapPost = gson.fromJson(jsonPost, new TypeToken<Map<String, Object>>() {
        }.getType());
        mapPost.put(LAST_UPDATED, FieldValue.serverTimestamp());
        Log.d("TAG", "gson post " + mapPost.toString());
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
