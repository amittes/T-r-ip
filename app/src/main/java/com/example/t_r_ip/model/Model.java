package com.example.t_r_ip.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static Model instance(){
        return _instance;
    }

    private Model() {
    }
}
