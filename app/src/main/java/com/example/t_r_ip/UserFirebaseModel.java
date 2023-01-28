package com.example.t_r_ip;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

public class UserFirebaseModel {
    private static final UserFirebaseModel _instance = new UserFirebaseModel();

    private static FirebaseFirestore db;
    private static FirebaseStorage storage;

    public static UserFirebaseModel instance() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        return _instance;
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    public static FirebaseStorage getStorage() {
        return storage;
    }

}
