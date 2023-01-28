package com.example.t_r_ip.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private final FirebaseModel firebaseModel = new FirebaseModel();

    private Model() {
    }

    public static Model instance() {
        return _instance;
    }

    public FirebaseModel getFirebaseModel() {
        return firebaseModel;
    }


    public interface Listener<T> {
        void onComplete(T data);
    }
}
